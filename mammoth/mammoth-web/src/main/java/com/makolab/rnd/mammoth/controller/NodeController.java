package com.makolab.rnd.mammoth.controller;

import com.makolab.rnd.mammoth.exception.BlockNotFound;
import com.makolab.rnd.mammoth.exception.CreatingBlockException;
import com.makolab.rnd.mammoth.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.model.chain.Block;
import com.makolab.rnd.mammoth.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.service.BlockService;
import com.makolab.rnd.mammoth.util.GensonUtils;
import com.makolab.rnd.mammoth.vocabulary.RdfFormat;
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
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Controller
@RequestMapping("block")
public class NodeController implements RestApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(NodeController.class);
  
  @Inject
  private BlockService blockService;

  @RequestMapping(
      value = "create",
      method = RequestMethod.POST,
      consumes = "text/turtle"
  )
  public ResponseEntity createBlock(@RequestParam(value = "graphIri", required = false) String graphIri,
                                    @RequestBody String rdfGraphContent) {
    LOGGER.debug("[POST] block/create ? graphIri={}", graphIri);
    LOGGER.trace("<rdfGraphContent>\n{}\n</rdfGraphContent>", rdfGraphContent);
    
    try {
      Block block = blockService.createBlock(graphIri, rdfGraphContent, RdfFormat.TURTLE);

      Genson genson = GensonUtils.createGenson();

      return ResponseEntity.ok(genson.serialize(block.getBlockHeader()));
    } catch (CreatingBlockException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
  
  @RequestMapping(
      value = "",
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
      value = "{blockIndex}",
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
}
