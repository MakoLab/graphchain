package com.makolab.rnd.mammoth.core.service;

import com.makolab.rnd.mammoth.core.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.core.ramt.RdfFormatsMapper;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

@Service
public class RdfSerializationHandler {

  private final Logger LOGGER = LoggerFactory.getLogger(RdfSerializationHandler.class);

  public String toJsonLd(String baseUri, String serializedRdf, RdfFormat rdfFormat) throws RdfSerializationException {
    // TODO: handle not supported RDF serialization formats
    RDFFormat rdf4jFormat = RdfFormatsMapper.toRdf4jFormat(rdfFormat).get();

    try {
      Model model = Rio.parse(toInputStream(serializedRdf), baseUri, rdf4jFormat);

      Writer writer = new StringWriter();
      Rio.write(model, writer, RDFFormat.JSONLD);

      return writer.toString();
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
