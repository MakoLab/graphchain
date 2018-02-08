package com.makolab.rnd.mammoth.core.model;

import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import java.util.Set;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class RdfDataset {

  private final Set<Triple> triples;

  public RdfDataset(Set<Triple> triples) {
    this.triples = triples;
  }

  public Set<Triple> getTriples() {
    return triples;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("{");
    boolean firstTriple = true;
    for (Triple triple : triples) {
      if (firstTriple) {
        firstTriple = false;
      } else {
        sb.append(", ");
      }
      sb.append("[");
      sb.append(triple.getSubject()).append(" -> ")
          .append(triple.getPredicate()).append(" -> ")
          .append(triple.getObject());
      sb.append("]");
    }
    sb.append("}");

    return sb.toString();
  }
}
