package com.makolab.rnd.mammoth.core.service.p2p;

import com.makolab.rnd.mammoth.core.exception.CreatingBlockException;
import com.makolab.rnd.mammoth.core.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.core.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.core.model.chain.Block;
import com.makolab.rnd.mammoth.core.model.chain.BlockContent;
import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.model.p2p.CommandType;
import com.makolab.rnd.mammoth.core.model.p2p.PeerToPeerCommand;
import com.makolab.rnd.mammoth.core.model.p2p.message.ErrorMessage;
import com.makolab.rnd.mammoth.core.model.p2p.message.Message;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jSerializationHandler;
import com.makolab.rnd.mammoth.core.service.BlockContentService;
import com.makolab.rnd.mammoth.core.service.BlockService;
import com.makolab.rnd.mammoth.core.service.base64.Base64Handler;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

@Service
public class PeerToPeerMessageHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerToPeerMessageHandler.class);

  private BlockService blockService;
  private Base64Handler base64Handler;
  private BlockContentService blockContentService;

  private Rdf4jSerializationHandler rdf4jSerializationHandler;
  private Rdf4jMapper rdf4jMapper;

  @Inject
  public PeerToPeerMessageHandler(BlockService blockService, Base64Handler base64Handler,
                                  BlockContentService blockContentService) {
    this.blockService = blockService;
    this.base64Handler = base64Handler;
    this.blockContentService = blockContentService;

    this.rdf4jSerializationHandler = new Rdf4jSerializationHandler();
    this.rdf4jMapper = new Rdf4jMapper();
  }

  public PeerToPeerCommand handleMessage(PeerToPeerMessage message) {
    LOGGER.debug("Handling message: {}", message);

    switch (message.messageType) {
      case QUERY_LATEST:
        return queryLatestBlock();
      case QUERY_ALL:
        return queryAllBlocks();
      case RESPONSE_BLOCKCHAIN:
        return responseBlockchain(message);
      default:
        String msg = String.format("Unknown type of P2P message: %s", message.messageType);
        throw new IllegalArgumentException(msg);
    }
  }

  private PeerToPeerCommand queryLatestBlock() {
    try {
      Block block = blockService.getLatestBlock();

      Message message = new PeerToPeerMessage(
          MessageType.RESPONSE_BLOCKCHAIN,
          Arrays.asList(block.getBlockHeader()),
          prepareGraphData(Arrays.asList(block.getBlockContent())));

      return new PeerToPeerCommand(CommandType.WRITE, message);
    } catch (ReadingBlockException ex) {
      LOGGER.debug("Exception was thrown:", ex);

      Message errorMessage = new ErrorMessage(ex.getMessage());

      return new PeerToPeerCommand(CommandType.WRITE_ERROR, errorMessage);
    }
  }

  private PeerToPeerCommand queryAllBlocks() {
    try {
      List<BlockHeader> allBlocks = blockService.getAllBlocks();
      List<BlockContent> allBlockContents = blockService.getAllBlockContents();

      Message message = new PeerToPeerMessage(
          MessageType.RESPONSE_BLOCKCHAIN,
          allBlocks,
          prepareGraphData(allBlockContents));

      return new PeerToPeerCommand(CommandType.WRITE, message);
    } catch (ReadingBlockException ex) {
      LOGGER.debug("Exception was thrown: ", ex);

      Message errorMessage = new ErrorMessage(ex.getMessage());

      return new PeerToPeerCommand(CommandType.WRITE_ERROR, errorMessage);
    }
  }

  private PeerToPeerCommand responseBlockchain(PeerToPeerMessage message) {
    try {
      Block latestBlock = blockService.getLatestBlock();

      if (message.data.size() > 0) {
        Collections.sort(message.data, Comparator.comparingInt(o -> Integer.parseInt(o.getIndex())));

        BlockHeader latestBlockReceived = message.data.get(message.data.size() - 1);
        if (latestBlockReceived.getIndexAsInt() > latestBlock.getBlockHeader().getIndexAsInt()) {
          LOGGER.debug("The index of the received latest block is bigger than the local one.");

          if (latestBlock.getBlockHeader().getHash().equals(latestBlockReceived.getPreviousHash())) {
            LOGGER.debug("The hash of the latest block is equal to the previous hash of the received block. " +
                "Adding new block...");
            try {
              blockService.addBlock(
                  latestBlockReceived,
                  obtainBlockContent(message.graph.get(message.graph.size() - 1)));
            } catch (RdfSerializationException ex) {
              LOGGER.warn("Unable to deserialize received graph as Turtle.", ex);
            }
          } else if (message.data.size() == 1) {
            LOGGER.debug("The received chain has only one link. Asking for whole chain.");
            Message returnMessage = new PeerToPeerMessage(MessageType.QUERY_ALL);
            return new PeerToPeerCommand(CommandType.BROADCAST, returnMessage);
          } else {
            LOGGER.debug("Replacing the whole blockchain.");
            try {
              blockService.replaceBlockchain(message.data, obtainBlockContents(message.graph));
            } catch (RdfSerializationException ex) {
              LOGGER.debug("Exception was thrown while serialization RDF graph.", ex);
              Message returnMessage = new PeerToPeerMessage(MessageType.ERROR);
              return new PeerToPeerCommand(CommandType.WRITE_ERROR, returnMessage);
            } catch (CreatingBlockException ex) {
              LOGGER.debug("Exception was thrown while creating blocks.", ex);
              Message returnMessage = new PeerToPeerMessage(MessageType.ERROR);
              return new PeerToPeerCommand(CommandType.WRITE_ERROR, returnMessage);
            }
          }
        } else {
          LOGGER.debug("The index of the received latest block is not bigger than the local one. Ignoring.");
        }
      }
    } catch (ReadingBlockException ex) {
      LOGGER.warn("Exception was thrown while reading block.", ex);
    }

    return new PeerToPeerCommand(CommandType.DO_NOTHING);
  }

  private List<PeerToPeerMessage.GraphData> prepareGraphData(List<BlockContent> blockContents) {
    List<PeerToPeerMessage.GraphData> result = new ArrayList<>();

    for (BlockContent blockContent : blockContents) {
      result.add(
          new PeerToPeerMessage.GraphData(
              blockContent.getDataGraphIri(),
              blockContentService.calculateBase64(blockContent)));
    }

    return result;
  }

  private List<BlockContent> obtainBlockContents(List<PeerToPeerMessage.GraphData> graphDatas)
      throws RdfSerializationException {
    List<BlockContent> result = new ArrayList<>();

    for (PeerToPeerMessage.GraphData graphData : graphDatas) {
      result.add(obtainBlockContent(graphData));
    }

    return result;
  }

  private BlockContent obtainBlockContent(PeerToPeerMessage.GraphData graphData) throws RdfSerializationException {
    Set<Triple> triples = getTriplesFromBase64(graphData.graphContent);
    return new BlockContent(graphData.graphIri, triples);
  }

  private Set<Triple> getTriplesFromBase64(String graphContentAsBase64) throws RdfSerializationException {
    String graphContentAsTurtle = base64Handler.toString(graphContentAsBase64);
    Model model = rdf4jSerializationHandler.deserializeModel(graphContentAsTurtle, RdfFormat.TURTLE);
    return rdf4jMapper.modelToTriples(model);
  }
}
