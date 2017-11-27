package com.makolab.rnd.mammoth.persistence.repository;

import com.makolab.rnd.mammoth.model.chain.LastBlockInfo;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.service.QueryTemplatesService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@org.springframework.stereotype.Repository
public class Rdf4jRepository implements TripleStoreRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(Rdf4jRepository.class);

  private static final int NUMBER_OF_ATTEMPTS = 3;

  @org.springframework.beans.factory.annotation.Value("${repository.repositoryUrl}")
  private String repositoryUrl;

  @org.springframework.beans.factory.annotation.Value("${repository.chainGraphIri}")
  private String chainGraphIri;

  @Inject
  private QueryTemplatesService queryTemplatesService;

  private Repository repository;
  private Rdf4jMapper rdf4jMapper;

  @Override
  @PostConstruct
  public void init() {
    LOGGER.debug("[CONFIG] Creating a triple store repository...");
    LOGGER.debug("\t[CONFIG] repositoryUrl={}", repositoryUrl);
    LOGGER.debug("\t[CONFIG] chainGraphIri={}", chainGraphIri);
    repository = new HTTPRepository(repositoryUrl);
    repository.initialize();
    rdf4jMapper = new Rdf4jMapper();
    LOGGER.info("[CONFIG] The triple store repository configured correctly.");
  }

  @Override
  @PreDestroy
  public void shutDown() {
    LOGGER.info("[CONFIG] Shutting down the triple store repository...");
    this.repository.shutDown();
  }

  /**
   * Checks whether the repository is ready to use, e.g. it contains the genesis Block.
   *
   * @return <code>true</code> if the repository is ready to use, <code>false</code> otherwise
   */
  @Override
  public boolean isReadyToUse() {
    ValueFactory valueFactory = repository.getValueFactory();

    try (RepositoryConnection rc = repository.getConnection()) {
      IRI metaGraphIri = valueFactory.createIRI(chainGraphIri);
      return rc.hasStatement(null, null, null, false, metaGraphIri);
    }
  }

  @Override
  public Optional<LastBlockInfo> getLastBlockInfo() {
    LOGGER.debug("Getting last block info...");
    String queryName = "get_last_block_info";
    LOGGER.trace("Invoking the '{}' SPARQL query...", queryName);
    String query = String.format(
        queryTemplatesService.getQueryTemplate(queryName).get(),
        chainGraphIri);

    try (RepositoryConnection rc = repository.getConnection()) {
      TupleQuery tupleQuery = rc.prepareTupleQuery(query);
      TupleQueryResult result = tupleQuery.evaluate();

      while (result.hasNext()) {
        BindingSet bindingSet = result.next();
        String lastBlockIri = bindingSet.getValue("lastBlockIRI").stringValue();
        String lastBlockHash = bindingSet.getValue("lastBlockHash").stringValue();
        String lastBlockIndex = bindingSet.getValue("lastBlockIndex").stringValue();

        result.close();

        return Optional.of(new LastBlockInfo(lastBlockIri, lastBlockHash, lastBlockIndex));
      }
    }

    return Optional.empty();
  }

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

  @Override
  public String makeQuery(String sparqlQuery) {
    try (RepositoryConnection rc = repository.getConnection()) {
      GraphQuery graphQuery = rc.prepareGraphQuery(sparqlQuery);

      StringWriter writer = new StringWriter();
      RDFWriter rdfWriter = Rio.createWriter(RDFFormat.JSONLD, writer);
      graphQuery.evaluate(rdfWriter);

      return writer.toString();
    }
  }

  @Override
  public void persistTriples(String graphIri, Set<Triple> triplesToPersist) {
    LOGGER.trace("Persisting triples into graph with IRI '{}'. Triples: {}", graphIri, triplesToPersist);

    ValueFactory vf = repository.getValueFactory();
    IRI contextIri = vf.createIRI(graphIri);

    Model model = rdf4jMapper.triplesToModel(triplesToPersist);

    boolean tryMore = true;

    for (int i = 0; i < NUMBER_OF_ATTEMPTS && tryMore ; i++) {
      try (RepositoryConnection rc = repository.getConnection()) {
        try {
          rc.begin();
          rc.add(model, contextIri);
          rc.commit();
          tryMore = false;
        } catch (Throwable ex) {
          LOGGER.error("Exception was thrown while adding a model to the repository.", ex);
          rc.rollback();
        }
      }
    }

    LOGGER.trace("Persisted {} triples into the <{}> graph.", triplesToPersist.size(), graphIri);
  }

  private Set<Triple> getTriplesForQuery(String query) {
    Set<Triple> result = new HashSet<>();

    try (RepositoryConnection rc = repository.getConnection()) {
      GraphQuery graphQuery = rc.prepareGraphQuery(query);
      GraphQueryResult graphQueryResult = graphQuery.evaluate();

      while (graphQueryResult.hasNext()) {
        Statement statement = graphQueryResult.next();
        result.add(rdf4jMapper.statementToTriple(statement));
      }

      graphQueryResult.close();
    }

    return result;
  }
}