package com.makolab.rnd.mammoth.core.model.rdf.impl;

import com.makolab.rnd.mammoth.core.model.rdf.Object;
import com.makolab.rnd.mammoth.core.vocabulary.rdf.RDF;
import com.makolab.rnd.mammoth.core.vocabulary.xml.XMLSchema;
import java.util.Objects;
import java.util.Optional;

public class Literal extends AbstractEntity implements Object {

  private final String lexicalForm;
  private final Iri datatype;
  private final Optional<String> languageTag;

  public Literal(String lexicalForm) {
    Objects.requireNonNull(lexicalForm, "'lexicalForm' parameter must not be null!");
    this.lexicalForm = lexicalForm;
    this.datatype = XMLSchema.string; // This is the default datatype's IRI when literal's datatype is not specified.
    this.languageTag = Optional.empty();
  }

  public Literal(String lexicalForm, Iri datatype) {
    Objects.requireNonNull(lexicalForm, "'lexicalForm' parameter must not be null!");
    Objects.requireNonNull(datatype, "'datatype' parameter must not be null!");
    this.lexicalForm = lexicalForm;
    this.datatype = datatype;
    this.languageTag = Optional.empty();
  }

  public Literal(String lexicalForm, String languageTag) {
    Objects.requireNonNull(lexicalForm, "'lexicalForm' parameter must not be null!");
    Objects.requireNonNull(languageTag, "'languageTag' parameter must not be null!");
    this.lexicalForm = lexicalForm;
    this.datatype = RDF.langString; // This is the default datatype's IRI for language-tagged strings.
    this.languageTag = Optional.ofNullable(languageTag);
  }

  public String getLexicalForm() {
    return lexicalForm;
  }

  public Iri getDatatype() {
    return datatype;
  }

  public Optional<String> getLanguageTag() {
    return languageTag;
  }

  @Override
  public String valueAsString() {
    return lexicalForm;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Literal literal = (Literal) o;
    return Objects.equals(getLexicalForm(), literal.getLexicalForm()) &&
        Objects.equals(getDatatype(), literal.getDatatype()) &&
        Objects.equals(getLanguageTag(), literal.getLanguageTag());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLexicalForm(), getDatatype(), getLanguageTag());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"")
        .append(getLexicalForm())
        .append("\"");
    if (getLanguageTag().isPresent()) {
      sb.append("@").append(getLanguageTag().get());
    } else {
      sb.append("^^<").append(getDatatype()).append(">");
    }
    return sb.toString();
  }
}