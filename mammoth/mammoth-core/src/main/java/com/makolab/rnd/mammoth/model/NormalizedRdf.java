package com.makolab.rnd.mammoth.model;

import com.makolab.rnd.mammoth.vocabulary.RdfFormat;

/**
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public class NormalizedRdf {

  private RdfDataset rdfDataset;
  private String serializedNormalizedObject;
  private RdfFormat serializationFormat;

  public NormalizedRdf(RdfDataset rdfDataset, String serializedNormalizedObject, RdfFormat serializationFormat) {
    this.rdfDataset = rdfDataset;
    this.serializedNormalizedObject = serializedNormalizedObject;
    this.serializationFormat = serializationFormat;
  }

  public RdfDataset getRdfDataset() {
    return rdfDataset;
  }

  public String getSerializedNormalizedObject() {
    return serializedNormalizedObject;
  }

  public RdfFormat getSerializationFormat() {
    return serializationFormat;
  }
}