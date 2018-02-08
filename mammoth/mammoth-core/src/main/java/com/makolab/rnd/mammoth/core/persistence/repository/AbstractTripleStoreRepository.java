package com.makolab.rnd.mammoth.core.persistence.repository;

import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.service.QueryTemplatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import javax.inject.Inject;

public abstract class AbstractTripleStoreRepository implements TripleStoreRepository {

  protected static final int NUMBER_OF_ATTEMPTS = 3;

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Inject
  private QueryTemplatesService queryTemplatesService;

  @org.springframework.beans.factory.annotation.Value("${repository.chainGraphIri}")
  private String chainGraphIri;

  @Override
  public Set<Triple> getBlockHeaderByIri(String blockIri) {
    LOGGER.debug("Getting block header triples by IRI '{}'.", blockIri);
    String queryName = "get_block_header_by_iri";
    LOGGER.trace("Invoking the '{}' SPARQL query...", queryName);
    String query = String.format(
        queryTemplatesService.getQueryTemplate(queryName).get(),
        blockIri,
        chainGraphIri,
        blockIri
    );

    return getTriplesForQuery(query);
  }

  @Override
  public Set<Triple> getGraphByIri(String graphIri) {
    LOGGER.debug("Getting graph by IRI '{}'.", graphIri);
    String queryName = "get_graph_by_iri";
    LOGGER.trace("Invoking the '{}' SPARQL query...", queryName);
    String query = String.format(
        queryTemplatesService.getQueryTemplate(queryName).get(),
        graphIri
    );

    return getTriplesForQuery(query);
  }

  public abstract Set<Triple> getTriplesForQuery(String query);
}