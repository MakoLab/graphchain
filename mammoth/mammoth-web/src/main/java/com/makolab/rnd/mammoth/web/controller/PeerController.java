package com.makolab.rnd.mammoth.web.controller;

import com.makolab.rnd.mammoth.core.model.p2p.message.AddPeerMessage;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.service.p2p.PeerToPeerService;
import com.makolab.rnd.mammoth.core.util.GensonUtils;
import com.owlike.genson.Genson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import javax.inject.Inject;

@Controller
@RequestMapping("peer")
public class PeerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerController.class);

  private PeerToPeerService peerToPeerService;

  @Inject
  public PeerController(PeerToPeerService peerToPeerService) {
    this.peerToPeerService = peerToPeerService;
  }

  @RequestMapping(
      value = "add",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity addPeer(@RequestBody String messageInJson) {
    LOGGER.debug("[POST] peer/add");
    LOGGER.trace("@RequestBody:\n<message>\n{}\n</message>", messageInJson);

    Genson genson = GensonUtils.createGenson();
    AddPeerMessage addPeerMessage = genson.deserialize(messageInJson, AddPeerMessage.class);
    if (!addPeerMessage.isMessageValid()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message is not valid.");
    }

    boolean addedSuccessfully = peerToPeerService.addPeer(addPeerMessage.peerAddress);

    if (addedSuccessfully) {
      return ResponseEntity.accepted().body("Added peer.\n");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to connect to a peer.");
    }
  }

  @RequestMapping(
      value = "",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity getAllPeers() {
    LOGGER.debug("[GET] peer");

    Genson genson = GensonUtils.createGenson();

    List<String> peers = peerToPeerService.getAllPeers();
    String serializedPeers = genson.serialize(peers);

    return ResponseEntity.ok(serializedPeers);
  }

  @RequestMapping(
      value = "broadcast",
      method = RequestMethod.GET
  )
  public ResponseEntity broadcast(
      @RequestParam(value = "messageType", defaultValue = "QUERY_LATEST") String messageType) {
    LOGGER.debug("[GET] peer/broadcast ? messageType = {}", messageType);

    peerToPeerService.broadcast(MessageType.valueOf(messageType));

    return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
  }
}