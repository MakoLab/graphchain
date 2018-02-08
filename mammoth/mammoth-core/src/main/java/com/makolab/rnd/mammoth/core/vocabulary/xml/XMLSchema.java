package com.makolab.rnd.mammoth.core.vocabulary.xml;

import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.core.vocabulary.VocabularyUtil;

public final class XMLSchema {

  public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

  public static final Iri anyURI = VocabularyUtil.createIri(NAMESPACE, "anyURI");
  public static final Iri decimal = VocabularyUtil.createIri(NAMESPACE, "decimal");
  public static final Iri string = VocabularyUtil.createIri(NAMESPACE, "string");
}
