package com.makolab.rnd.mammoth.model.rdf.impl;

import com.makolab.rnd.mammoth.model.rdf.Object;
import com.makolab.rnd.mammoth.model.rdf.Subject;
import java.util.Objects;

public class BlankNode extends AbstractEntity implements Subject, Object {

  private String identifier;

  public BlankNode(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  @Override
  public String toString() {
    return "BlankNode{" +
        "identifier='" + identifier + '\'' +
        '}';
  }

  @Override
  public String valueAsString() {
    return identifier;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlankNode other = (BlankNode) o;
    return Objects.equals(getIdentifier(), other.getIdentifier());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIdentifier());
  }
}
