package com.makolab.rnd.mammoth.service;

import com.makolab.rnd.mammoth.exception.ExtractingRdfDatasetException;
import com.makolab.rnd.mammoth.model.RdfDataset;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.vocabulary.RdfFormat;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Service
public class RdfDatasetExtractor {

  private Rdf4jMapper rdf4jMapper;

  public RdfDatasetExtractor() {
    this.rdf4jMapper = new Rdf4jMapper();
  }

  public RdfDataset extractRdfDatasetFromRawString(String baseUri, String rawRdf, RdfFormat rdfFormat)
      throws ExtractingRdfDatasetException {
    InputStream is = obtainInputStream(rawRdf);
    RDFFormat rdf4jFormat = obtainRdf4jInternalRdfFormat(rdfFormat);

    try {
      Model model = Rio.parse(is, baseUri, rdf4jFormat);

      Set<Triple> triples = new HashSet<>();

      // TODO: replace with one method
      for (Statement statement : model) {
        triples.add(rdf4jMapper.statementToTriple(statement));
      }

      return new RdfDataset(triples);
    } catch (IOException ex) {
      throw new ExtractingRdfDatasetException(ex.getMessage());
    }
  }

  private InputStream obtainInputStream(String rawRdf) {
    return IOUtils.toInputStream(rawRdf, Charsets.UTF_8);
  }

  private RDFFormat obtainRdf4jInternalRdfFormat(RdfFormat rdfFormat)
      throws ExtractingRdfDatasetException {
    switch (rdfFormat) {
      case TURTLE: {
        return RDFFormat.TURTLE;
      }
      case JSON_LD: {
        return RDFFormat.JSONLD;
      }
      default: {
        String exceptionMsg = "Only the Turtle serialization format is supported for now.";
        throw new ExtractingRdfDatasetException(exceptionMsg);
      }
    }
  }
}