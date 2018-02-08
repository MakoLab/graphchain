package com.makolab.rnd.mammoth.core.service.normalization;

import com.makolab.rnd.mammoth.core.exception.NormalizingRdfException;
import com.makolab.rnd.mammoth.core.model.NormalizedRdf;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import java.util.Set;

public interface NormalizationService {

  NormalizedRdf normalizeRdf(Set<Triple> triples) throws NormalizingRdfException;
}
