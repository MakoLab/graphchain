package com.makolab.rnd.mammoth.core.service.p2p;

import com.makolab.rnd.mammoth.core.exception.PeerToPeerConnectionException;
import com.makolab.rnd.mammoth.core.model.chain.Block;
import com.makolab.rnd.mammoth.core.model.p2p.PeerToPeerCommand;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.makolab.rnd.mammoth.core.service.BlockContentService;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

@Service
public class PeerToPeerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerToPeerService.class);

  @Value("${p2p.server.port}")
  private String serverPort;

  private final PeerToPeerMessageHandler peerToPeerMessageHandler;
  private final BlockContentService blockContentService;

  private WebSocketEndpoint webSocketEndpoint;

  @Inject
  public PeerToPeerService(PeerToPeerMessageHandler peerToPeerMessageHandler, BlockContentService blockContentService) {
    this.peerToPeerMessageHandler = peerToPeerMessageHandler;
    this.blockContentService = blockContentService;
  }

  @PostConstruct
  public void init() {
    LOGGER.debug("Init P2P service...");

    int port = Integer.parseInt(serverPort);

    this.webSocketEndpoint = new WebSocketEndpoint(this, port);
  }

  @PreDestroy
  public void close() {
    this.webSocketEndpoint.close();
  }

  public boolean addPeer(String peerAddress) {
    try {
      webSocketEndpoint.connectToPeer(peerAddress);
      return true;
    } catch (PeerToPeerConnectionException ex) {
      LOGGER.debug("Unable to connect to a peer. Details:", ex);
      return false;
    }
  }

  public List<String> getAllPeers() {
    List<String> result = new ArrayList<>();

    for (WebSocket socket : webSocketEndpoint.getSockets()) {
      result.add(socket.getRemoteSocketAddress().getHostName() +
          ":" +
          socket.getRemoteSocketAddress().getPort());
    }

    return result;
  }

  public PeerToPeerCommand handleMessage(PeerToPeerMessage message) {
    return peerToPeerMessageHandler.handleMessage(message);
  }

  public void broadcast(MessageType messageType) {
    PeerToPeerMessage message = new PeerToPeerMessage(messageType);
    peerToPeerMessageHandler.handleMessage(message);

    webSocketEndpoint.broadcast(message);
  }

  public void broadcastBlockCreation(MessageType messageType, Block block) {
    String blockContent = blockContentService.calculateBase64(block.getBlockContent());
    PeerToPeerMessage message = new PeerToPeerMessage(messageType,
        Arrays.asList(block.getBlockHeader()),
        Arrays.asList(
            new PeerToPeerMessage.GraphData(
                block.getBlockHeader().getDataGraphIri(),
                blockContent)));

    webSocketEndpoint.broadcast(message);
  }
}