package com.makolab.rnd.mammoth.vocabulary.bc;

import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.vocabulary.VocabularyUtil;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class MammothVocabulary {

  public static final String NAMESPACE = "http://www.ontologies.makolab.com/mammoth/";

  /*
   * Classes
   */
  public static final String LAST_BLOCK = NAMESPACE + "LastBlock";

  /*
   * Properties
   */
  public static final String HAS_INTERNAL_LABEL = NAMESPACE + "hasInternalLabel";

  /*
   * IRIs - Classes
   */
  public static final Iri LastBlock = VocabularyUtil.createIri(LAST_BLOCK);

  /*
   * IRIs - Properties
   */
  public static final Iri hasInternalLabel = VocabularyUtil.createIri(HAS_INTERNAL_LABEL);
}
