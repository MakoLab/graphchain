package com.makolab.rnd.mammoth.vocabulary.owl;

import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.vocabulary.VocabularyUtil;

public class OWL {

  public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";

  public static final Iri NamedIndividual = VocabularyUtil.createIri(NAMESPACE, "NamedIndividual");
}
