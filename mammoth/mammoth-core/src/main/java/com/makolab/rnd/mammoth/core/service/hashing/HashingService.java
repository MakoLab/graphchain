package com.makolab.rnd.mammoth.core.service.hashing;

import com.makolab.rnd.mammoth.core.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import java.util.Set;

public interface HashingService {

  String calculateHash(Set<Triple> triples) throws CalculatingHashException;

  Set<Triple> handleTriplesBeforePersisting(Set<Triple> triples);

  Set<Triple> handleTriplesBeforeHashing(Set<Triple> triples);
}
