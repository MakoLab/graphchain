package com.makolab.rnd.mammoth.examples;

import com.makolab.rnd.mammoth.core.exception.RdfSerializationException;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jSerializationHandler;
import com.makolab.rnd.mammoth.core.ramt.serialization.NTriplesFormatHandler;
import com.makolab.rnd.mammoth.core.service.cryptography.HashCalculator;
import com.makolab.rnd.mammoth.core.service.cryptography.Sha256HashCalculator;
import com.makolab.rnd.mammoth.core.service.hashing.CircleDotHashingService;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalculateHashesForTriplesExample {

  private static final Logger LOGGER = LoggerFactory.getLogger(CalculateHashesForTriplesExample.class);

  public static void main(String[] args) throws IOException, RdfSerializationException {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(Rdf4jSerializationHandler.class);
    context.register(Rdf4jMapper.class);
    context.register(Sha256HashCalculator.class);
    context.register(CircleDotHashingService.class);
    context.register(NTriplesFormatHandler.class);
    context.refresh();

    Rdf4jSerializationHandler rdf4jSerializationHandler = context.getBean(Rdf4jSerializationHandler.class);
    Rdf4jMapper rdf4jMapper = context.getBean(Rdf4jMapper.class);
    CircleDotHashingService circleDotHashingService = context.getBean(CircleDotHashingService.class);
    NTriplesFormatHandler nTriplesFormatHandler = context.getBean(NTriplesFormatHandler.class);
    HashCalculator hashCalculator = context.getBean(Sha256HashCalculator.class);

    for (Map.Entry<String, String> entry : getSerializedModels().entrySet()) {
      Model model = rdf4jSerializationHandler.deserializeModel(entry.getValue(), RdfFormat.TURTLE);
      Set<Triple> triples = rdf4jMapper.modelToTriples(model);
      triples = circleDotHashingService.handleTriplesBeforeHashing(triples);
      for (Triple triple : triples) {
        String serializedTriple = nTriplesFormatHandler.serializeTriple(triple);
        String hashForSerializedTriple = hashCalculator.calculateHash(serializedTriple);
        LOGGER.info("{}, {}", serializedTriple, hashForSerializedTriple);
      }
    }
  }

  private static Map<String, String> getSerializedModels() throws IOException {
    Map<String, String> result = new HashMap<>();

    File file = new File("mammoth-examples/src/main/resources/leis/2594007XIACKNMUAW223.ttl");
    String serializedModel = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
    result.put(file.getName(), serializedModel);

    return result;
  }
}