package com.makolab.rnd.mammoth.ramt.serialization;

import com.makolab.rnd.mammoth.model.rdf.Triple;

public class NTriplesFormatHandler {

  public String serializeTriple(Triple triple) {
    return triple.getSubject() + " " + triple.getPredicate() + " " + triple.getObject() + ".";
  }
}
