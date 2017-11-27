package com.makolab.rnd.mammoth.service.normalization;

import com.makolab.rnd.mammoth.exception.NormalizingRdfException;
import com.makolab.rnd.mammoth.model.NormalizedRdf;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import java.util.Set;

public interface NormalizationService {

  NormalizedRdf normalizeRdf(Set<Triple> triples) throws NormalizingRdfException;
}
