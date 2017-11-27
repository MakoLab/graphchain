package com.makolab.rnd.mammoth.service.hashing;

import com.makolab.rnd.mammoth.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.exception.NormalizingRdfException;
import com.makolab.rnd.mammoth.model.NormalizedRdf;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.service.cryptography.HashCalculator;
import com.makolab.rnd.mammoth.service.normalization.NormalizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Set;
import javax.inject.Inject;

@Service
public class JsonLdHashingService implements HashingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonLdHashingService.class);

  @Inject
  private HashCalculator hashCalculator;

  @Inject
  private NormalizationService jsonLdNormalizationService;

  @Override
  public String calculateHash(Set<Triple> triples) throws CalculatingHashException {
    LOGGER.debug("Calculating hash for {} triples.", triples.size());

    try {
      NormalizedRdf normalizedRdf = jsonLdNormalizationService.normalizeRdf(triples);

      return hashCalculator.calculateHash(normalizedRdf.getSerializedNormalizedObject());
    } catch (NormalizingRdfException ex) {
      throw new CalculatingHashException("Exception was thrown while calculating hash for triple.", ex);
    }
  }

  @Override
  public Set<Triple> handleTriplesBeforeHashing(Set<Triple> triples) {
    return triples;
  }

  @Override
  public Set<Triple> handleTriplesBeforePersisting(Set<Triple> triples) {
    // Nothing to do here.
    return triples;
  }
}