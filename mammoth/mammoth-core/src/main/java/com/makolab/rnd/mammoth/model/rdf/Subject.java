package com.makolab.rnd.mammoth.model.rdf;

import com.makolab.rnd.mammoth.model.rdf.impl.BlankNode;
import com.makolab.rnd.mammoth.model.rdf.impl.Iri;

public interface Subject {

  public BlankNode asBlankNode();

  public boolean isBlankNode();

  public Iri asIri();

  public boolean isIri();

  public String valueAsString();
}
