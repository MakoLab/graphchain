package com.makolab.rnd.mammoth.ramt;

import com.makolab.rnd.mammoth.vocabulary.RdfFormat;
import org.eclipse.rdf4j.rio.RDFFormat;
import java.util.Optional;

public class RdfFormatsMapper {

  public static Optional<RdfFormat> toNativeFormat(RDFFormat rdf4jFormat) {
    if (rdf4jFormat.equals(RDFFormat.RDFXML)) {
      return Optional.of(RdfFormat.RDF_XML);
    } else if (rdf4jFormat.equals(RDFFormat.JSONLD)) {
      return Optional.of(RdfFormat.JSON_LD);
    } else if (rdf4jFormat.equals(RDFFormat.TURTLE)) {
      return Optional.of(RdfFormat.TURTLE);
    } else if (rdf4jFormat.equals(RDFFormat.NQUADS)) {
      return Optional.of(RdfFormat.N_QUADS);
    } else {
      return Optional.empty();
    }
  }

  public static Optional<RDFFormat> toRdf4jFormat(RdfFormat rdfFormat) {
    switch (rdfFormat) {
      case RDF_XML:
        return Optional.of(RDFFormat.RDFXML);
      case JSON_LD:
        return Optional.of(RDFFormat.JSONLD);
      case TURTLE:
        return Optional.of(RDFFormat.TURTLE);
      case N_QUADS:
        return Optional.of(RDFFormat.NQUADS);
      default:
        return Optional.empty();
    }
  }
}
