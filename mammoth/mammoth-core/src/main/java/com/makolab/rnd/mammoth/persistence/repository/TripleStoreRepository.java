package com.makolab.rnd.mammoth.persistence.repository;

import com.makolab.rnd.mammoth.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import java.util.Optional;
import java.util.Set;

public interface TripleStoreRepository {

  void init();

  void shutDown();

  /**
   * Checks whether the repository is ready to use, e.g. it contains the genesis block.
   *
   * @return <code>true</code> if the repository is ready to use, <code>false</code> otherwise
   */
  boolean isReadyToUse();

  Optional<LastBlockInfo> getLastBlockInfo();

  Set<Triple> getBlockHeaderByIri(String blockIri);

  Set<Triple> getGraphByIri(String graphIri);

  String makeQuery(String sparqlQuery);

  void persistTriples(String graphIri, Set<Triple> triplesToPersist);
}
