package com.makolab.rnd.mammoth.vocabulary.bc;

import com.makolab.rnd.mammoth.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.vocabulary.VocabularyUtil;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class GCO {

  public static final String MAKOLAB_BC = "http://www.ontologies.makolab.com/bc";
  public static final String MAKOLAB_BC_NAMESPACE = "http://www.ontologies.makolab.com/bc/";

  /*
   * Classes
   */
  public static final String BLOCK = MAKOLAB_BC_NAMESPACE + "Block";
  public static final String GENESIS_BLOCK = MAKOLAB_BC_NAMESPACE + "GenesisBlock";

  /*
   * Properties
   */
  public static final String HAS_DATA_GRAPH_IRI = MAKOLAB_BC_NAMESPACE + "hasDataGraphIRI";
  public static final String HAS_DATA_HASH = MAKOLAB_BC_NAMESPACE + "hasDataHash";
  public static final String HAS_HASH = MAKOLAB_BC_NAMESPACE + "hasHash";
  public static final String HAS_INDEX = MAKOLAB_BC_NAMESPACE + "hasIndex";
  public static final String HAS_PREVIOUS_BLOCK = MAKOLAB_BC_NAMESPACE + "hasPreviousBlock";
  public static final String HAS_PREVIOUS_HASH = MAKOLAB_BC_NAMESPACE + "hasPreviousHash";
  public static final String HAS_TIME_STAMP = MAKOLAB_BC_NAMESPACE + "hasTimeStamp";

  /*
   * IRIs - Classes
   */
  public static final Iri Block = VocabularyUtil.createIri(BLOCK);
  public static final Iri GenesisBlock = VocabularyUtil.createIri(GENESIS_BLOCK);

  /*
   * IRIs - Properties
   */
  public static final Iri hasDataGraphIri = VocabularyUtil.createIri(HAS_DATA_GRAPH_IRI);
  public static final Iri hasDataHash = VocabularyUtil.createIri(HAS_DATA_HASH);
  public static final Iri hasHash = VocabularyUtil.createIri(HAS_HASH);
  public static final Iri hasIndex = VocabularyUtil.createIri(HAS_INDEX);
  public static final Iri hasPreviousBlock = VocabularyUtil.createIri(HAS_PREVIOUS_BLOCK);
  public static final Iri hasPreviousHash = VocabularyUtil.createIri(HAS_PREVIOUS_HASH);
  public static final Iri hasTimeStamp = VocabularyUtil.createIri(HAS_TIME_STAMP);
}