package com.makolab.rnd.mammoth.core.persistence;

import com.makolab.rnd.mammoth.core.exception.BlockNotFound;
import com.makolab.rnd.mammoth.core.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.core.exception.CreatingBlockException;
import com.makolab.rnd.mammoth.core.exception.ObtainingBlockHeaderException;
import com.makolab.rnd.mammoth.core.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.core.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.core.model.chain.Block;
import com.makolab.rnd.mammoth.core.model.chain.BlockContent;
import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.core.model.rdf.impl.Literal;
import com.makolab.rnd.mammoth.core.persistence.repository.TripleStoreRepository;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jSerializationHandler;
import com.makolab.rnd.mammoth.core.service.cryptography.HashCalculator;
import com.makolab.rnd.mammoth.core.service.hashing.HashingService;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import com.makolab.rnd.mammoth.core.vocabulary.bc.GCO;
import com.makolab.rnd.mammoth.core.vocabulary.owl.OWL;
import com.makolab.rnd.mammoth.core.vocabulary.rdf.RDF;
import com.makolab.rnd.mammoth.core.vocabulary.xml.XMLSchema;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Service
public class RepositoryManager {

  public static final String DEFAULT_GENESIS_BLOCK_PREVIOUS_HASH = "0";
  public static final String DEFAULT_GENESIS_BLOCK_TIME_STAMP = "1502269780";
  public static final String DEFAULT_GENESIS_BLOCK_DATA_GRAPH_IRI = GCO.MAKOLAB_BC;

  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryManager.class);

  @Value("${repository.chainGraphIri}")
  private String chainGraphIri;

  @Inject
  private FileContentObtainer fileContentObtainer;

  @Inject
  private HashCalculator hashCalculator;

  @Inject
  private HashingService hashingService;

  @Inject
  private TripleStoreRepository repository;

  private Rdf4jMapper rdf4jMapper;
  private Rdf4jSerializationHandler rdf4jSerializationHandler;

  public RepositoryManager() {
    this.rdf4jMapper = new Rdf4jMapper();
    this.rdf4jSerializationHandler = new Rdf4jSerializationHandler();
  }

  @PostConstruct
  public void init() {
    LOGGER.debug("[CONFIG] Initializing the repository manager...");

    if (repository.isReadyToUse()) {
      LOGGER.debug("[CONFIG] The repository is already set to use.");
    } else {
      LOGGER.debug("[CONFIG] Creating a genesis Block...");
      try {
        createGenesisBlock();
      } catch (CreatingBlockException ex) {
        throw new RuntimeException("Exception was thrown while creating genesis block.", ex);
      }
    }

    LOGGER.info("[CONFIG] The repository manager is configured correctly.");
  }

  public LastBlockInfo getLastBlockInfo() throws ReadingBlockException {
    LastBlockInfo lastBlockInfo = repository.getLastBlockInfo()
        .orElseThrow(() -> new ReadingBlockException("Unable to create last block info."));
    LOGGER.debug("Last block info: {}", lastBlockInfo);
    return lastBlockInfo;
  }

  public Block getBlockByIri(String blockIri) throws ReadingBlockException {
    try {
      Set<Triple> blockHeaderTriples = repository.getBlockHeaderByIri(blockIri);
      if (blockHeaderTriples.isEmpty()) {
        String msg = String.format("Block with IRI '%s' was not found.", blockIri);
        throw new BlockNotFound(msg);
      }
      BlockHeader blockHeader = createBlockHeaderFromTriples(blockHeaderTriples);
      Set<Triple> blockContentTriples = repository.getGraphByIri(blockHeader.getDataGraphIri());
      BlockContent blockContent = new BlockContent(blockHeader.getDataGraphIri(), blockContentTriples);

      return new Block(blockIri, blockHeader, blockContent);
    } catch (ObtainingBlockHeaderException ex) {
      throw new ReadingBlockException("Exception was thrown while creating block header from triples.", ex);
    }
  }

  public String makeSparqlQuery(String sparqlQuery) {
    return repository.makeSparqlQueryAndReturnGraphAsJsonLdString(sparqlQuery);
  }

  public void persistBlock(Block blockToPersist, boolean shouldHandleTriplesBeforePersisting) {
    Set<Triple> blockHeaderTriplesToPersist = new HashSet<>();

    BlockHeader blockHeader = blockToPersist.getBlockHeader();
    BlockContent blockContent = blockToPersist.getBlockContent();

    Iri blockIri = new Iri(blockToPersist.getBlockIri());
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        RDF.type,
        GCO.Block
    ));
    if (blockToPersist.isGenesisBlock()) {
      blockHeaderTriplesToPersist.add(new Triple(
          blockIri,
          RDF.type,
          GCO.GenesisBlock
      ));
    }
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        RDF.type,
        OWL.NamedIndividual
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasDataGraphIri,
        new Literal(blockHeader.getDataGraphIri(), XMLSchema.anyURI)
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasDataHash,
        new Literal(blockHeader.getDataHash())
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasHash,
        new Literal(blockHeader.getHash())
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasIndex,
        new Literal(blockHeader.getIndex(), XMLSchema.decimal)
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasPreviousBlock,
        new Iri(blockHeader.getPreviousBlock())
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasPreviousHash,
        new Literal(blockHeader.getPreviousHash())
    ));
    blockHeaderTriplesToPersist.add(new Triple(
        blockIri,
        GCO.hasTimeStamp,
        new Literal(blockHeader.getTimestamp(), XMLSchema.decimal)
    ));

    Set<Triple> blockContentTriplesToPersist;
    if (shouldHandleTriplesBeforePersisting) {
      blockContentTriplesToPersist = hashingService.handleTriplesBeforePersisting(blockContent.getTriples());
    } else {
      blockContentTriplesToPersist = blockContent.getTriples();
    }

    // TODO: The following invocations should be in the same transaction.
    repository.persistTriples(chainGraphIri, blockHeaderTriplesToPersist);
    repository.persistTriples(blockContent.getDataGraphIri(), blockContentTriplesToPersist);
  }

  public void clearBlockchainAndPersistBlocks(List<Block> blocksToPersist) {
    // TODO: This should be run in the same transaction.
    repository.clearRepository();

    for (Block block : blocksToPersist) {
      persistBlock(block, false);
    }

    LOGGER.debug("The repository has been cleared and repopulated with obtained blocks. New number of blocks: {}",
        blocksToPersist.size());
  }

  private void createGenesisBlock() throws CreatingBlockException {
    String newIndex = "0";
    String dataGraphIri = DEFAULT_GENESIS_BLOCK_DATA_GRAPH_IRI;
    String newBlockIri = chainGraphIri + "/" + newIndex;

    try {
      Model deserializedModel = rdf4jSerializationHandler.deserializeModel(
          dataGraphIri,
          getRawContentOfGcoOntology(),
          RdfFormat.TURTLE);
      Set<Triple> triples = rdf4jMapper.modelToTriples(deserializedModel);

      String previousBlock = newBlockIri;
      String previousHash = DEFAULT_GENESIS_BLOCK_PREVIOUS_HASH;
      String timestamp = DEFAULT_GENESIS_BLOCK_TIME_STAMP;

      String dataHash = hashingService.calculateHash(triples);
      String stringToCalculateHash =
          (newIndex + newBlockIri + previousHash + timestamp + dataGraphIri + dataHash).trim();
      String hash = hashCalculator.calculateHash(stringToCalculateHash);

      BlockHeader blockHeader = new BlockHeader(
          dataGraphIri,
          dataHash,
          hash,
          newIndex,
          previousBlock,
          previousHash,
          timestamp);
      BlockContent blockContent = new BlockContent(dataGraphIri, triples);

      Block genesisBlock = new Block(newBlockIri, blockHeader, blockContent, true);

      persistBlock(genesisBlock, true);
    } catch (CalculatingHashException | RdfSerializationException ex) {
      throw new CreatingBlockException("Exception was thrown while creating genesis block.", ex);
    }
  }

  private BlockHeader createBlockHeaderFromTriples(Set<Triple> triples) throws ObtainingBlockHeaderException {
    LOGGER.trace("Attempt to create block header from triples: {}", triples);
    String dataGraphIri = null;
    String dataHash = null;
    String hash = null;
    String index = null;
    String previousBlock = null;
    String previousHash = null;
    String timeStamp = null;

    for (Triple triple : triples) {
      String predicate = triple.getPredicate().valueAsString();
      String value = triple.getObject().valueAsString();

      switch (predicate) {
        case GCO.HAS_DATA_GRAPH_IRI:
          dataGraphIri = value;
          break;
        case GCO.HAS_DATA_HASH:
          dataHash = value;
          break;
        case GCO.HAS_HASH:
          hash = value;
          break;
        case GCO.HAS_INDEX:
          index = value;
          break;
        case GCO.HAS_PREVIOUS_BLOCK:
          previousBlock = value;
          break;
        case GCO.HAS_PREVIOUS_HASH:
          previousHash = value;
          break;
        case GCO.HAS_TIME_STAMP:
          timeStamp = value;
          break;
        case RDF.TYPE:
          // Omitting RDF#type predicate.
          break;
        default:
          LOGGER.warn("Unknown predicate in the triple: '{}', '{}', '{}'",
              triple.getSubject(),
              predicate,
              value);
          break;
      }
    }

    if (previousHash != null && hash != null && timeStamp != null) {
      return new BlockHeader(dataGraphIri, dataHash, hash, index, previousBlock, previousHash, timeStamp);
    } else {
      String blockSummary =
          String.format(
              "[previousHash = '%s', hash = '%s', timestamp = '%s']",
              previousHash, hash, timeStamp);
      throw new ObtainingBlockHeaderException("Incorrect info concerning Block:" + blockSummary);
    }
  }

  private String getRawContentOfGcoOntology() {
    LOGGER.debug("Getting GCO ontology content.");
    return fileContentObtainer.getFileContent("/ontologies/ontobc.ttl").get();
  }
}