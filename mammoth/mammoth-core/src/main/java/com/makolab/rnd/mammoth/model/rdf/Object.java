package com.makolab.rnd.mammoth.model.rdf;

import com.makolab.rnd.mammoth.model.rdf.impl.BlankNode;
import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.model.rdf.impl.Literal;

public interface Object {

  public BlankNode asBlankNode();

  public boolean isBlankNode();

  public Iri asIri();

  public boolean isIri();

  public Literal asLiteral();

  public boolean isLiteral();

  public String valueAsString();
}
