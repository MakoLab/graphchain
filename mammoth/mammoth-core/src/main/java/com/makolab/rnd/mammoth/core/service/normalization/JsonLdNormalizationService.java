package com.makolab.rnd.mammoth.core.service.normalization;

import com.makolab.rnd.mammoth.core.exception.NormalizingRdfException;
import com.makolab.rnd.mammoth.core.model.NormalizedRdf;
import com.makolab.rnd.mammoth.core.model.RdfDataset;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.vocabulary.RdfFormat;
import com.github.jsonldjava.core.JsonLdConsts;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author Rafał Trójczak (Rafal.Trojczak@makolab.net)
 */
@Service
public class JsonLdNormalizationService implements NormalizationService {

  private final Logger LOGGER = LoggerFactory.getLogger(JsonLdNormalizationService.class);

  private Rdf4jMapper rdf4jMapper;

  public JsonLdNormalizationService() {
    this.rdf4jMapper = new Rdf4jMapper();
  }

  @Override
  public NormalizedRdf normalizeRdf(Set<Triple> triples) throws NormalizingRdfException {
    Model model = rdf4jMapper.triplesToModel(triples);
    String modelAsJsonLd = rdf4jMapper.modelToSerialization(model, RDFFormat.JSONLD);
    try {
      Object jsonObject = JsonUtils.fromString(modelAsJsonLd);
      Object normalizedJsonObject = JsonLdProcessor.normalize(jsonObject, prepareJsonLdOptions());

      return new NormalizedRdf(new RdfDataset(triples), normalizedJsonObject.toString(), RdfFormat.JSON_LD);
    } catch (IOException | JsonLdError ex) {
      throw new NormalizingRdfException("Exception was thrown while normalizing JSON object.", ex);
    }
  }

  private JsonLdOptions prepareJsonLdOptions() {
    JsonLdOptions options = new JsonLdOptions();
    options.format = JsonLdConsts.APPLICATION_NQUADS;
    return options;
  }
}