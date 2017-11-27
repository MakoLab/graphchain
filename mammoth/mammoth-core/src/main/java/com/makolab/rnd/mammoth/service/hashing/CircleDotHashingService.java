package com.makolab.rnd.mammoth.service.hashing;

import com.makolab.rnd.mammoth.model.rdf.Object;
import com.makolab.rnd.mammoth.model.rdf.Subject;
import com.makolab.rnd.mammoth.model.rdf.Triple;
import com.makolab.rnd.mammoth.model.rdf.impl.BlankNode;
import com.makolab.rnd.mammoth.model.rdf.impl.Literal;
import com.makolab.rnd.mammoth.ramt.serialization.NTriplesFormatHandler;
import com.makolab.rnd.mammoth.service.cryptography.HashCalculator;
import com.makolab.rnd.mammoth.vocabulary.bc.MammothVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

@Service
public class CircleDotHashingService implements HashingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CircleDotHashingService.class);

  private static final int HASH_SIZE = 256;

  @Inject
  private HashCalculator hashCalculator;

  private NTriplesFormatHandler nTriplesFormatHandler;

  private BigInteger modOperand = BigInteger.valueOf(2).pow(HASH_SIZE);

  public CircleDotHashingService() {
    this.nTriplesFormatHandler = new NTriplesFormatHandler();
  }

  @Override
  public String calculateHash(Set<Triple> triples) {
    LOGGER.debug("Calculating hash for {} triples.", triples.size());

    BigInteger finalDigest = BigInteger.ZERO;

    for (Triple triple : triples) {
      String serializedTriple = nTriplesFormatHandler.serializeTriple(triple);
      byte[] hash = hashCalculator.calculateHashAsBytes(serializedTriple);
      BigInteger hashAsNumber = new BigInteger(1, hash);
      finalDigest = finalDigest.add(hashAsNumber).mod(modOperand);
    }

    return finalDigest.toString(16);
  }

  @Override
  public Set<Triple> handleTriplesBeforePersisting(Set<Triple> triples) {
    Set<Triple> resultTriples = new HashSet<>();

    for (Triple triple : triples) {
      Subject subject = triple.getSubject();
      if (subject.isBlankNode()) {
        resultTriples.add(
            new Triple(
                subject,
                MammothVocabulary.hasInternalLabel,
                new Literal(subject.asBlankNode().getIdentifier())));
      }
    }

    resultTriples.addAll(triples);

    return resultTriples;
  }

  @Override
  public Set<Triple> handleTriplesBeforeHashing(Set<Triple> triples) {
    Set<Triple> result = new HashSet<>();

    Map<String, String> actualIdsToInternalIds = new HashMap<>();

    for (Triple triple : triples) {
      if (triple.getPredicate().equals(MammothVocabulary.hasInternalLabel)) {
        String actualId = triple.getSubject().asBlankNode().getIdentifier();
        String internalId = triple.getObject().asLiteral().getLexicalForm();
        actualIdsToInternalIds.put(actualId, internalId);
      }
    }

    LOGGER.debug("actualIdsToInternalIds={}", actualIdsToInternalIds);

    for (Triple triple : triples) {
      Subject subject = triple.getSubject();
      Object object = triple.getObject();

      if (triple.getPredicate().equals(MammothVocabulary.hasInternalLabel)) {
        // We do not want to add this triple, so we do nothing.
      } else if (subject.isBlankNode() || object.isBlankNode()) {
        if (subject.isBlankNode()) {
          subject = new BlankNode(actualIdsToInternalIds.get(subject.asBlankNode().getIdentifier()));
        }
        if (object.isBlankNode()) {
          object = new BlankNode(actualIdsToInternalIds.get(object.asBlankNode().getIdentifier()));
        }

        result.add(new Triple(subject, triple.getPredicate(), object));
      } else {
        result.add(triple);
      }
    }

    return result;
  }
}
