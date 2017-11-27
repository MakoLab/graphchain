package com.makolab.rnd.mammoth.vocabulary.rdf;

import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.vocabulary.VocabularyUtil;

public final class RDF {

  public static final String NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

  public static final String LANG_STRING = NAMESPACE + "langString";
  public static final String TYPE = NAMESPACE + "type";

  /*
   * IRIs - Properties
   */
  public static final Iri langString = VocabularyUtil.createIri(LANG_STRING);
  public static final Iri type = VocabularyUtil.createIri(TYPE);
}