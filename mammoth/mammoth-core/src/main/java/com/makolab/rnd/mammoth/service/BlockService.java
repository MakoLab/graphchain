package com.makolab.rnd.mammoth.service;

import com.makolab.rnd.mammoth.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.exception.CreatingBlockException;
import com.makolab.rnd.mammoth.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.model.chain.Block;
import com.makolab.rnd.mammoth.model.chain.BlockContent;
import com.makolab.rnd.mammoth.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.persistence.RepositoryManager;
import com.makolab.rnd.mammoth.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.ramt.Rdf4jSerializationHandler;
import com.makolab.rnd.mammoth.service.cryptography.HashCalculator;
import com.makolab.rnd.mammoth.service.hashing.HashingService;
import com.makolab.rnd.mammoth.util.TimestampCreator;
import com.makolab.rnd.mammoth.vocabulary.RdfFormat;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Service
public class BlockService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlockService.class);

  @Value("${repository.chainGraphIri}")
  private String chainGraphIri;

  @Inject
  private HashCalculator hashCalculator;

  @Inject
  private HashingService hashingService;

  @Inject
  private RepositoryManager repositoryManager;

  private Rdf4jSerializationHandler rdf4jSerializationHandler;
  private Rdf4jMapper rdf4jMapper;

  private Long lastIndex;

  public BlockService() {
    this.rdf4jSerializationHandler = new Rdf4jSerializationHandler();
    this.rdf4jMapper = new Rdf4jMapper();
  }

  @PostConstruct
  public void init() {
    LOGGER.debug("[CONFIG] Initializing the block service...");
    try {
      LastBlockInfo lastBlockInfo = repositoryManager.getLastBlockInfo();
      lastIndex = Long.valueOf(lastBlockInfo.getBlockIndex());
    } catch (ReadingBlockException ex) {
      LOGGER.error("Exception occurred while tried to determine last index.");
      throw new RuntimeException("Unable to determine the last index value. Aborting initialization of BlockService.");
    }
    LOGGER.info("The block service initialized successfully.");
  }

  public Block createBlock(String dataGraphIri, String rawRdf, RdfFormat rdfFormat) throws CreatingBlockException {
    LOGGER.debug("Creating block with graphIri '{}' and rdfFormat '{}'...", dataGraphIri, rdfFormat.getJenaName());

    try {
      Set<Triple> triples = getTriplesFromSerializedModel(rawRdf, rdfFormat);

      Long newIndex = generateNewIndex();
      String newBlockIri = generateNewBlockIri(newIndex);

      LastBlockInfo lastBlockInfo = repositoryManager.getLastBlockInfo();
      String previousBlock = lastBlockInfo.getBlockIri();
      String previousHash = lastBlockInfo.getBlockHash();
      String timestamp = TimestampCreator.createTimestampString();

      String dataHash = hashingService.calculateHash(triples);
      String stringToCalculateHash =
          (newIndex + previousBlock + previousHash + timestamp + dataGraphIri + dataHash).trim();
      String hash = hashCalculator.calculateHash(stringToCalculateHash);

      BlockHeader blockHeader = new BlockHeader(
          dataGraphIri,
          dataHash,
          hash,
          newIndex.toString(),
          previousBlock,
          previousHash,
          timestamp);
      BlockContent blockContent = new BlockContent(dataGraphIri, triples);

      Block blockToStore = new Block(newBlockIri, blockHeader, blockContent);

      repositoryManager.persistBlock(blockToStore);

      // MAYBE: Maybe this should be obtained from Triple Store in order to avoid some kind of inconsistency.
      lastIndex++;

      return getBlock(newIndex.toString());
    } catch (ReadingBlockException ex) {
      String msg = "Exception was thrown while getting information about the last block.";
      throw new CreatingBlockException(msg, ex);
    } catch (RdfSerializationException ex) {
      String msg = String.format("Exception was thrown while deserializing RDF model from '%s' format.", rdfFormat);
      throw new CreatingBlockException(msg, ex);
    } catch (CalculatingHashException ex) {
      throw new CreatingBlockException("Exception was thrown while calculating hash.", ex);
    }
  }

  public List<BlockHeader> getAllBlocks() throws ReadingBlockException {
    List<BlockHeader> blockHeaders = new ArrayList<>();

    boolean getPreviousBlock = true;

    LastBlockInfo lastBlockInfo = repositoryManager.getLastBlockInfo();
    String blockIriToObtain = lastBlockInfo.getBlockIri();

    while (getPreviousBlock) {
      Block block = getBlockByIri(blockIriToObtain);

      blockHeaders.add(block.getBlockHeader());

      blockIriToObtain = block.getBlockHeader().getPreviousBlock();

      if (blockIriToObtain.equals(block.getBlockIri())) {
        LOGGER.debug("Block '{}' is the first block.", blockIriToObtain);
        getPreviousBlock = false;
      }
    }

    return blockHeaders;
  }

  public Block getBlock(String blockIndex) throws ReadingBlockException {
    String blockIri = chainGraphIri + "/" + blockIndex;
    return getBlockByIri(blockIri);
  }

  public Block getBlockByIri(String blockIri) throws ReadingBlockException {
    return repositoryManager.getBlockByIri(blockIri);
  }

  private Set<Triple> getTriplesFromSerializedModel(String serializedModel, RdfFormat rdfFormat)
      throws RdfSerializationException {
    Model deserializeModel = rdf4jSerializationHandler.deserializeModel(serializedModel, rdfFormat);
    return rdf4jMapper.modelToTriples(deserializeModel);
  }

  private Long generateNewIndex() {
    Long newIndex = lastIndex + 1;
    LOGGER.debug("Generated new index: {}", newIndex);
    return newIndex;
  }

  private String generateNewBlockIri(Long newIndex) {
    return chainGraphIri + "/" + newIndex;
  }
}
