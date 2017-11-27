package com.makolab.rnd.mammoth.service.hashing;

import com.makolab.rnd.mammoth.exception.CalculatingHashException;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import java.util.Set;

public interface HashingService {

  String calculateHash(Set<Triple> triples) throws CalculatingHashException;

  Set<Triple> handleTriplesBeforePersisting(Set<Triple> triples);

  Set<Triple> handleTriplesBeforeHashing(Set<Triple> triples);
}
