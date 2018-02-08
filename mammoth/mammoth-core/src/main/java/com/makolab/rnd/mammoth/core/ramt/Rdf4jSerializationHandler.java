package com.makolab.rnd.mammoth.core.ramt;

import com.makolab.rnd.mammoth.core.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import java.io.IOException;
import java.io.InputStream;

public class Rdf4jSerializationHandler {

  public Model deserializeModel(String serializedModel, RdfFormat rdfFormat) throws RdfSerializationException {
    return deserializeModel("", serializedModel, rdfFormat);
  }

  public Model deserializeModel(String baseUri, String serializedModel, RdfFormat rdfFormat)
      throws RdfSerializationException {
    // TODO: Handle 'Optional' properly
    RDFFormat rdf4jFormat = RdfFormatsMapper.toRdf4jFormat(rdfFormat).get();

    try {
      return Rio.parse(toInputStream(serializedModel), baseUri, rdf4jFormat);
    } catch (IOException ex) {
      String msg = String.format(
          "Unable to parse serialized RDF from '%s' format.",
          rdfFormat.getJenaName());
      throw new RdfSerializationException(msg, ex);
    }
  }

  private InputStream toInputStream(String string) throws IOException {
    return IOUtils.toInputStream(string, "UTF-8"); // Assuming this is a proper UTF-8 encoded string.
  }
}