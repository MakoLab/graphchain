package com.makolab.rnd.mammoth.core.model.rdf;

import java.util.Objects;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class Triple {
  
  private final Subject subject;
  private final Predicate predicate;
  private final Object object;

  public Triple(Subject subject, Predicate predicate, Object object) {
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
  }

  public Subject getSubject() {
    return subject;
  }

  public Predicate getPredicate() {
    return predicate;
  }

  public Object getObject() {
    return object;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Triple triple = (Triple) o;
    return Objects.equals(getSubject(), triple.getSubject()) &&
        Objects.equals(getPredicate(), triple.getPredicate()) &&
        Objects.equals(getObject(), triple.getObject());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSubject(), getPredicate(), getObject());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(")
        .append(getSubject()).append(", ")
        .append(getPredicate()).append(", ")
        .append(getObject())
        .append(")");
    return sb.toString();
  }
}
