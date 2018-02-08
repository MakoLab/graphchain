package com.makolab.rnd.mammoth.core.vocabulary.owl;

import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.core.vocabulary.VocabularyUtil;

public class OWL {

  public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";

  public static final Iri NamedIndividual = VocabularyUtil.createIri(NAMESPACE, "NamedIndividual");
}
