package com.makolab.rnd.mammoth.core.persistence.repository.impl;

import com.makolab.rnd.mammoth.core.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.persistence.repository.AbstractTripleStoreRepository;
import com.makolab.rnd.mammoth.core.persistence.repository.RepositoryHelper;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.service.QueryTemplatesService;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGGraphQuery;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import com.franz.agraph.repository.AGValueFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class AleografTripleStoreRepository extends AbstractTripleStoreRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(AleografTripleStoreRepository.class);

  @Value("${repository.ag.repositoryUrl}")
  private String agRepositoryUrl;

  @Value("${repository.ag.username}")
  private String agUsername;

  @Value("${repository.ag.password}")
  private String agPassword;

  @Value("${repository.ag.repositoryId}")
  private String agRepositoryId;

  @Value("${repository.chainGraphIri}")
  private String chainGraphIri;

  @Inject
  private QueryTemplatesService queryTemplatesService;

  private AGServer server;
  private AGCatalog catalog;
  private AGRepository repository;

  private Rdf4jMapper rdf4jMapper;

  @Override
  @PostConstruct
  public void init() {
    LOGGER.debug("[CONFIG] Creating a triple store repository...");
    LOGGER.debug("\t[CONFIG] agRepositoryUrl={}", agRepositoryUrl);
    LOGGER.debug("\t[CONFIG] agUsername={}", agUsername);
    LOGGER.debug("\t[CONFIG] agPassword={}", "********");
    LOGGER.debug("\t[CONFIG] chainGraphIri={}", chainGraphIri);
    server = new AGServer(agRepositoryUrl, agUsername, agPassword);
    catalog = server.getRootCatalog();
    repository = catalog.openRepository(agRepositoryId);

    rdf4jMapper = new Rdf4jMapper();
    LOGGER.info("[CONFIG] The triple store repository configured correctly.");
  }

  @Override
  @PreDestroy
  public void shutDown() {
    LOGGER.info("[CONFIG] Shutting down the triple store repository...");
    this.repository.close();
    this.server.close();
  }

  @Override
  public boolean isReadyToUse() {
    AGValueFactory valueFactory = repository.getValueFactory();

    AGRepositoryConnection rc = repository.getConnection();
    IRI metaGraphIri = valueFactory.createIRI(chainGraphIri);
    return rc.hasStatement(null, null, null, false, metaGraphIri);
  }

  @Override
  public Optional<LastBlockInfo> getLastBlockInfo() {
    LOGGER.debug("Getting last block info...");
    String queryName = "get_last_block_info";
    LOGGER.trace("Invoking the '{}' SPARQL query...", queryName);
    String query = String.format(
        queryTemplatesService.getQueryTemplate(queryName).get(),
        chainGraphIri);

    AGRepositoryConnection rc = null;
    try {
      rc = repository.getConnection();
      TupleQuery tupleQuery = rc.prepareTupleQuery(query);
      TupleQueryResult result = tupleQuery.evaluate();

      Optional<LastBlockInfo> lastBlockInfo = RepositoryHelper.extractOptionalLastBlockInfo(result);
      result.close();

      return lastBlockInfo;
    } finally {
      if (rc != null) {
        rc.close();
      }
    }
  }

  @Override
  public String makeSparqlQueryAndReturnGraphAsJsonLdString(String sparqlQuery) {
    AGRepositoryConnection rc = null;
    try {
      rc = repository.getConnection();
      AGGraphQuery graphQuery = rc.prepareGraphQuery(QueryLanguage.SPARQL, sparqlQuery);

      StringWriter writer = new StringWriter();
      RDFWriter rdfWriter = Rio.createWriter(RDFFormat.JSONLD, writer);
      graphQuery.evaluate(rdfWriter);

      return writer.toString();
    } finally {
      if (rc != null) {
        rc.close();
      }
    }
  }

  @SuppressWarnings("Duplicates")
  @Override
  public void persistTriples(String graphIri, Set<Triple> triplesToPersist) {
    LOGGER.trace("Persisting triples into graph with IRI '{}'. Triples: {}", graphIri, triplesToPersist);

    AGValueFactory vf = repository.getValueFactory();
    IRI contextIri = vf.createIRI(graphIri);

    Model modelToPersist = rdf4jMapper.triplesToModel(triplesToPersist);

    boolean tryMore = true;

    for (int i = 0; i < NUMBER_OF_ATTEMPTS && tryMore; i++) {
      AGRepositoryConnection rc = null;
      try {
        rc = repository.getConnection();
        // This enables us creating blank nodes with ID from an input graph.
        rc.getHttpRepoClient().setAllowExternalBlankNodeIds(true);

        try {
          rc.begin();
          rc.add(modelToPersist, contextIri);
          rc.commit();
          tryMore = false;
        } catch (Throwable ex) {
          LOGGER.error("Exception was thrown while adding a model to the repository.", ex);
          rc.rollback();
        }
      } finally {
        if (rc != null) {
          rc.close();
        }
      }
    }

    LOGGER.trace("Persisted {} triples into the <{}> graph.", triplesToPersist.size(), graphIri);
  }

  @Override
  public Set<Triple> getTriplesForQuery(String query) {
    Set<Triple> result = new HashSet<>();

    AGRepositoryConnection rc = null;
    try {
      rc = repository.getConnection();
      AGGraphQuery graphQuery = rc.prepareGraphQuery(QueryLanguage.SPARQL, query);
      GraphQueryResult graphQueryResult = graphQuery.evaluate();

      while (graphQueryResult.hasNext()) {
        Statement statement = graphQueryResult.next();
        result.add(rdf4jMapper.statementToTriple(statement));
      }

      graphQueryResult.close();
    } finally {
      if (rc != null) {
        rc.close();
      }
    }

    return result;
  }

  @Override
  public void clearRepository() {
    AGRepositoryConnection rc = null;
    try {
      LOGGER.debug("Clearing the whole repository.");
      rc = repository.getConnection();
      rc.clear();
    } finally {
      if (rc != null) {
        rc.close();
      }
    }
  }
}
