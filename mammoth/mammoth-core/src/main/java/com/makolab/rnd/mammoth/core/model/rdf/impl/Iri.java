package com.makolab.rnd.mammoth.core.model.rdf.impl;

import com.makolab.rnd.mammoth.core.model.rdf.Object;
import com.makolab.rnd.mammoth.core.model.rdf.Predicate;
import com.makolab.rnd.mammoth.core.model.rdf.Subject;
import java.util.Objects;

public class Iri extends AbstractEntity implements Subject, Predicate, Object {

  private final String iriInternalValue;

  public Iri(String iri) {
    this.iriInternalValue = iri;
  }

  @Override
  public String valueAsString() {
    return iriInternalValue;
  }

  @Override
  public String toString() {
    return iriInternalValue;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Iri other = (Iri) o;
    return Objects.equals(iriInternalValue, other.iriInternalValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iriInternalValue);
  }
}
