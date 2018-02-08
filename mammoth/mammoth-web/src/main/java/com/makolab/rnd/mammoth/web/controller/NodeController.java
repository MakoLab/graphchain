package com.makolab.rnd.mammoth.web.controller;

import com.makolab.rnd.mammoth.core.exception.BlockNotFound;
import com.makolab.rnd.mammoth.core.exception.CreatingBlockException;
import com.makolab.rnd.mammoth.core.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.core.model.chain.Block;
import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.service.BlockService;
import com.makolab.rnd.mammoth.core.service.p2p.PeerToPeerService;
import com.makolab.rnd.mammoth.core.util.GensonUtils;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import com.makolab.rnd.mammoth.validator.model.GraphChainValidationResult;
import com.makolab.rnd.mammoth.validator.service.ValidationService;
import com.owlike.genson.Genson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Controller
@RequestMapping
public class NodeController implements RestApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(NodeController.class);

  private BlockService blockService;
  private PeerToPeerService peerToPeerService;
  private ValidationService validationService;

  @Inject
  public NodeController(BlockService blockService, PeerToPeerService peerToPeerService,
                        ValidationService validationService) {
    this.blockService = blockService;
    this.peerToPeerService = peerToPeerService;
    this.validationService = validationService;
  }

  @RequestMapping(
      value = "block/create",
      method = RequestMethod.POST,
      consumes = "text/turtle"
  )
  public ResponseEntity createBlock(@RequestParam(value = "graphIri", required = false) String graphIri,
                                    @RequestBody String rdfGraphContent) {
    LOGGER.debug("[POST] block/create ? graphIri={}", graphIri);
    LOGGER.trace("<rdfGraphContent>\n{}\n</rdfGraphContent>", rdfGraphContent);

    try {
      Block block = blockService.createBlock(graphIri, rdfGraphContent, RdfFormat.TURTLE);
      LOGGER.debug("Block created successfully. Broadcasting change.");
      LOGGER.trace("\tCreated block details: {}", block);
      peerToPeerService.broadcastBlockCreation(MessageType.RESPONSE_BLOCKCHAIN, block);

      Genson genson = GensonUtils.createGenson();

      return ResponseEntity.ok(genson.serialize(block.getBlockHeader()));
    } catch (CreatingBlockException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @RequestMapping(
      value = "block",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity getAllBlocks() {
    LOGGER.debug("[GET] block");

    try {
      List<BlockHeader> allBlocks = blockService.getAllBlocks();

      Genson genson = GensonUtils.createGenson();

      return ResponseEntity.ok(genson.serialize(allBlocks));
    } catch (ReadingBlockException ex) {
      LOGGER.warn("Exception was thrown while getting all blocks.", ex);
      return ResponseEntity.noContent().build();
    }
  }

  @RequestMapping(
      value = "block/{blockIndex}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity getBlock(@PathVariable("blockIndex") String blockIndex) {
    LOGGER.debug("[GET] block/{}", blockIndex);

    try {
      Block block = blockService.getBlock(blockIndex);

      Genson genson = GensonUtils.createGenson();

      return ResponseEntity.ok(genson.serialize(block.getBlockHeader()));
    } catch (BlockNotFound ex) {
      return ResponseEntity.notFound().build();
    } catch (ReadingBlockException ex) {
      String msg = String.format("Exception was thrown while getting the block with index '%s'.", blockIndex);
      LOGGER.warn(msg, ex);
      return ResponseEntity.notFound().build();
    }
  }

  @RequestMapping(
      value = "validate",
      method = RequestMethod.GET
  )
  public ResponseEntity validate() {
    LOGGER.debug("[GET] validate");

    GraphChainValidationResult result = validationService.validateGraphChain();

    return ResponseEntity.ok(result.isValid() + "\n");
  }
}
