package com.makolab.rnd.mammoth.core.model.chain;

import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import java.util.Objects;
import java.util.Set;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class BlockContent {

  private String dataGraphIri;
  private Set<Triple> triples;

  public BlockContent(String dataGraphIri, Set<Triple> triples) {
    this.dataGraphIri = dataGraphIri;
    this.triples = triples;
  }

  public String getDataGraphIri() {
    return dataGraphIri;
  }

  public Set<Triple> getTriples() {
    return triples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockContent that = (BlockContent) o;
    return Objects.equals(getDataGraphIri(), that.getDataGraphIri()) &&
        Objects.equals(getTriples(), that.getTriples());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDataGraphIri(), getTriples());
  }
}