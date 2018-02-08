package com.makolab.rnd.mammoth.core.model.rdf;

import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;

public interface Predicate {

  Iri asIri();

  boolean isIri();

  String valueAsString();
}
