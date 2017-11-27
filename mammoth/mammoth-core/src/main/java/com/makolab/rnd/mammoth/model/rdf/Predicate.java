package com.makolab.rnd.mammoth.model.rdf;

import com.makolab.rnd.mammoth.model.rdf.impl.Iri;

public interface Predicate {

  Iri asIri();

  boolean isIri();

  String valueAsString();
}
