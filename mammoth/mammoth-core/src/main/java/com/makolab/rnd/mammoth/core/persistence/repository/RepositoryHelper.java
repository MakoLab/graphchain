package com.makolab.rnd.mammoth.core.persistence.repository;

import com.makolab.rnd.mammoth.core.model.chain.LastBlockInfo;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class RepositoryHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryHelper.class);

  public static Optional<LastBlockInfo> extractOptionalLastBlockInfo(TupleQueryResult result) {
    try {
      while (result.hasNext()) {
        BindingSet bindingSet = result.next();
        String lastBlockIri = bindingSet.getValue("lastBlockIRI").stringValue();
        String lastBlockHash = bindingSet.getValue("lastBlockHash").stringValue();
        String lastBlockIndex = bindingSet.getValue("lastBlockIndex").stringValue();

        return Optional.of(new LastBlockInfo(lastBlockIri, lastBlockHash, lastBlockIndex));
      }
    } catch (Throwable throwable) {
      LOGGER.warn("Exception was thrown while extracting last block info.", throwable);
    }

    return Optional.empty();
  }
}
