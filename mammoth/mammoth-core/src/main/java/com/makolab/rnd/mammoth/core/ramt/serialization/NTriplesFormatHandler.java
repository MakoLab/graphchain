package com.makolab.rnd.mammoth.core.ramt.serialization;

import com.makolab.rnd.mammoth.core.model.rdf.Triple;

public class NTriplesFormatHandler {

  public String serializeTriple(Triple triple) {
    String subject;
    if (triple.getSubject().isIri()) {
      subject = "<" + triple.getSubject().asIri().valueAsString() + ">";
    } else { // is blank node
      // we are talking currently hold internal serialization of a blank node
      subject = triple.getSubject().asBlankNode().getIdentifier();
    }

    String predicate = "<" + triple.getPredicate().asIri().valueAsString() + ">";

    String object;
    if (triple.getObject().isIri()) {
      object = "<" + triple.getObject().asIri().valueAsString() + ">";
    } else if (triple.getObject().isBlankNode()) {
      object = triple.getObject().asBlankNode().getIdentifier();
    } else {
      object = triple.getObject().asLiteral().toString();
    }

    return subject + " " + predicate + " " + object + " .";
  }
}