package com.makolab.rnd.mammoth.core.service.p2p;

import com.makolab.rnd.mammoth.core.exception.PeerToPeerConnectionException;
import com.makolab.rnd.mammoth.core.model.p2p.PeerToPeerCommand;
import com.makolab.rnd.mammoth.core.model.p2p.message.Message;
import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.makolab.rnd.mammoth.core.util.GensonUtils;
import com.owlike.genson.Genson;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpoint.class);

  private final PeerToPeerService peerToPeerService;
  private final WebSocketServer server;

  private List<WebSocket> sockets = new ArrayList<>();

  public WebSocketEndpoint(PeerToPeerService peerToPeerService, int port) {
    this.peerToPeerService = peerToPeerService;
    this.server = new WebSocketServer(new InetSocketAddress(port)) {

      @Override
      public void onStart() {
        LOGGER.info("Starting web socket server on port '{}'...");
      }

      @Override
      public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        LOGGER.debug("Opening web socket connection...");
        sockets.add(webSocket);
      }

      @Override
      public void onMessage(WebSocket webSocket, String s) {
        handleMessage(webSocket, s);
      }

      @Override
      public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        LOGGER.debug("Closing connection to a peer ('{}').", this.getAddress().toString());
        sockets.remove(webSocket);
      }

      @Override
      public void onError(WebSocket webSocket, Exception ex) {
        LOGGER.warn("Exception concerning web sockets was thrown. Details:", ex);
        sockets.remove(webSocket);
      }
    };
    server.start();
    LOGGER.info("[CONFIG] Web socket server is listening on the port '{}'...", Integer.toString(port));
  }

  public void connectToPeer(String peerAddress) throws PeerToPeerConnectionException {
    try {
      final WebSocketClient socket = new WebSocketClient(new URI(peerAddress)) {

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
          LOGGER.debug("Opening web socket connection to a peer ('{}').", peerAddress);
          sockets.add(this);
        }

        @Override
        public void onMessage(String s) {
          handleMessage(this, s);
        }

        @Override
        public void onClose(int i, String s, boolean b) {
          LOGGER.debug("Closing connection to a peer '{}'.", this.getRemoteSocketAddress());
          sockets.remove(this);
        }

        @Override
        public void onError(Exception ex) {
          LOGGER.warn("Exception concerning web sockets was thrown. Details:", ex);
          sockets.remove(this);
        }
      };
      socket.connect();
      LOGGER.info("Connected to a peer ('{}').", peerAddress);
    } catch (URISyntaxException ex) {
      throw new PeerToPeerConnectionException("Unable to connect to a peer because its address is invalid.", ex);
    }
  }

  public List<WebSocket> getSockets() {
    return this.sockets;
  }

  public void broadcast(Message message) {
    LOGGER.debug("Handling broadcast of the message '{}'.", message.toString());
    for (WebSocket socket : sockets) {
      LOGGER.debug("Sending message to '{}'.", socket.getRemoteSocketAddress());
      socket.send(message.serializeAsJson());
    }
  }

  public void close() {
    LOGGER.debug("Closing sockets...");
    for (WebSocket socket : sockets) {
      socket.close();
    }
    LOGGER.info("Closing web socket endpoint...");
    try {
      this.server.stop();
    } catch (IOException | InterruptedException ex) {
      LOGGER.warn("Exception was thrown while closing web socket server.", ex);
    }
  }

  private void handleMessage(WebSocket socket, String messageAsString) {
    LOGGER.debug("Received new message from peer '{}'.", socket.getRemoteSocketAddress());

    Genson genson = GensonUtils.createGensonWithConverters();
    PeerToPeerMessage message = genson.deserialize(messageAsString, PeerToPeerMessage.class);

    LOGGER.debug("Handling message from peer '{}': {}", socket.getRemoteSocketAddress().toString(), message);

    PeerToPeerCommand command = peerToPeerService.handleMessage(message);
    LOGGER.debug("Handling command with type '{}' and message type '{}'.",
        command.commandType,
        command.getMessageType());

    switch (command.commandType) {
      case WRITE:
        socket.send(genson.serialize(command.message));
        break;
      case WRITE_ERROR:
        socket.send(genson.serialize(command.message));
        break;
      case BROADCAST:
        socket.send(genson.serialize(command.message));
        break;
      case DO_NOTHING:
        LOGGER.debug("Doing nothing.");
        break;
      default:
        LOGGER.warn("Unknown command type '{}'.", command.commandType);
        break;
    }
  }
}
