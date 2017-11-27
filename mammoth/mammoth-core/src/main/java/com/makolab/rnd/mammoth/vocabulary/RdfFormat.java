package com.makolab.rnd.mammoth.vocabulary;

import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.JSON_LD_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.N_QUADS_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.N_TRIPLES_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.RDF_JSON_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.RDF_THRIFT_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.RDF_XML_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.TEXT_TURTLE_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.TRIG_VALUE;
import static com.makolab.rnd.mammoth.vocabulary.SemanticWebMimeType.TRIX_VALUE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
public enum RdfFormat {

  TURTLE("TURTLE", TEXT_TURTLE_VALUE, "ttl", "n3"),
  N_TRIPLES("N-TRIPLE", N_TRIPLES_VALUE, "nt"),
  N_QUADS("N-QUADS", N_QUADS_VALUE, "nq"),
  TRIG("TRIG", TRIG_VALUE, "trig"),
  RDF_XML("RDF/XML", RDF_XML_VALUE, "rdf", "owl"),
  JSON_LD("JSON-LD", JSON_LD_VALUE, "jsonld"),
  JSON_LD_NON_EXPANDED("JSON-LD", JSON_LD_VALUE, "jsonld"),  
  RDF_THRIFT("RDF-THRIFT", RDF_THRIFT_VALUE, "trdf", "rt"),
  RDF_JSON("RDF/JSON", RDF_JSON_VALUE, "rj"),
  TRIX("TRIX", TRIX_VALUE, "trix");

  private final String mimeType;
  private final String jenaName;
  private final List<String> extensions = new ArrayList<>();

  private RdfFormat(String jenaName, String mimeType, String... exts) {
    this.jenaName = jenaName;
    this.mimeType = mimeType;
    this.extensions.addAll(Arrays.asList(exts));
  }


  /**
   * Returns the Jena name for this serialization format. Returned name is used in some of Jena's
   * methods.
   *
   * @return String with Jena name
   */
  public String getJenaName() {
    return jenaName;
  }

  /**
   * Returns the MIME type for this serialization format.
   *
   * @return String with MIME type
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Returns a proper RDF format for the extension.
   *
   * @param extension an extension by which RDF format should be found
   * @return serialization format
   */
  public static RdfFormat getByExtension(String extension) {
    for (RdfFormat value : values()) {
      for (String ext : value.extensions) {
        if (ext.equals(extension)) {
          return value;
        }
      }
    }
    
    String excepctionMsg = 
        String.format("There isn't any RDF format with '%s'.", extension);
    throw new IllegalArgumentException(excepctionMsg);
  }

  /**
   * Returns a proper RDF format for the MIME type.
   *
   * @param mimeType a MIME type by which RDF format should be found
   * @return RDF format
   */
  public static RdfFormat getByMimeType(String mimeType) {
    for (RdfFormat value : values()) {
      if (value.getMimeType().equals(mimeType)) {
        return value;
      }
    }
    
    String exceptionMsg = String.format("There is no element with MIME type '%s'.", mimeType);
    throw new IllegalArgumentException(exceptionMsg);
  }
}
