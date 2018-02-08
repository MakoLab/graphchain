package com.makolab.rnd.mammoth.core.vocabulary;

import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;

public class VocabularyUtil {

  public static Iri createIri(String namespace, String localName ) {
    return new Iri(namespace + localName);
  }

  public static Iri createIri(String iriString) {
    return new Iri(iriString);
  }
}
