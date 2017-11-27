package com.makolab.rnd.mammoth.validator.service;

import com.makolab.rnd.mammoth.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.exception.ReadingBlockException;
import com.makolab.rnd.mammoth.model.chain.Block;
import com.makolab.rnd.mammoth.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.persistence.RepositoryManager;
import com.makolab.rnd.mammoth.service.hashing.HashingService;
import com.makolab.rnd.mammoth.validator.model.BlockValidationResult;
import com.makolab.rnd.mammoth.validator.model.GraphChainValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

@Service
public class ValidationService {

  private final Logger LOGGER = LoggerFactory.getLogger(ValidationService.class);

  @Inject
  private HashingService hashingService;

  @Inject
  private RepositoryManager repositoryManager;

  @Inject
  private BlockHashValidator blockHashValidator;

  public GraphChainValidationResult validateGraphChain() {
    boolean isValid = false;

    List<BlockValidationResult> blocks = new ArrayList<>();

    try {
      LastBlockInfo lastBlockInfo = repositoryManager.getLastBlockInfo();
      LOGGER.debug("Last block IRI: '{}'", lastBlockInfo.getBlockIri());

      boolean getPreviousBlock = true;
      String blockIriToObtain = lastBlockInfo.getBlockIri();

      while (getPreviousBlock) {
        Block currentBlock = repositoryManager.getBlockByIri(blockIriToObtain);

        Set<Triple> triples = hashingService.handleTriplesBeforeHashing(currentBlock.getBlockContent().getTriples());
        String calculatedDataHash = hashingService.calculateHash(triples);

        BlockValidationResult blockValidationResult =
            blockHashValidator.validateBlock(currentBlock, calculatedDataHash);
        if (blockValidationResult.isValid()) {
          LOGGER.debug("Block '{}' is valid.", currentBlock.getBlockIri());
        } else {
          LOGGER.debug("Block '{}' is not valid. Details: {}", currentBlock.getBlockIri(), blockValidationResult);
          getPreviousBlock = false;
        }
        blocks.add(blockValidationResult);

        blockIriToObtain = currentBlock.getBlockHeader().getPreviousBlock();

        if (isGenesisBlock(currentBlock)) {
          // for the first block IRI and its previous block IRI are the same,
          // so we do not go further
          LOGGER.debug("Block '{}' has '{}' as the previous block; they are equal.",
              blockIriToObtain,
              currentBlock.getBlockIri());
          getPreviousBlock = false;
          isValid = true;
        }
      }
    } catch (ReadingBlockException | CalculatingHashException ex) {
      LOGGER.error("Exception was thrown while normalizing rdf graph.", ex);
    }

    return new GraphChainValidationResult(isValid, blocks);
  }

  private boolean isGenesisBlock(Block block) {
    return block.getBlockIri().equals(block.getBlockHeader().getPreviousBlock());
  }
}
