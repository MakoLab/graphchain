package com.makolab.rnd.mammoth.core.model.rdf.impl;

public abstract class AbstractEntity {

  public BlankNode asBlankNode() {
    return (BlankNode) this;
  }

  public boolean isBlankNode() {
    return getClass().equals(BlankNode.class);
  }

  public Iri asIri() {
    return (Iri) this;
  }

  public boolean isIri() {
    return getClass().equals(Iri.class);
  }

  public Literal asLiteral() {
    return (Literal) this;
  }

  public boolean isLiteral() {
    return getClass().equals(Literal.class);
  }
}
