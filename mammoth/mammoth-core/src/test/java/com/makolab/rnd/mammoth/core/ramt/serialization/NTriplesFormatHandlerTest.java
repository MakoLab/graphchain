package com.makolab.rnd.mammoth.core.ramt.serialization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import com.makolab.rnd.mammoth.core.model.rdf.Triple;
import com.makolab.rnd.mammoth.core.model.rdf.impl.Iri;
import com.makolab.rnd.mammoth.core.model.rdf.impl.Literal;
import org.junit.Before;
import org.junit.Test;

public class NTriplesFormatHandlerTest {

  private static final String IRI_NAMESPACE = "http://example.com/";

  private NTriplesFormatHandler triplesFormatHandler;

  @Before
  public void setUp() {
    this.triplesFormatHandler = new NTriplesFormatHandler();
  }

  @Test
  public void shouldFormatTripleInNTripleSerialization() {
    Triple triple = new Triple(
        new Iri(IRI_NAMESPACE + "class1"),
        new Iri(IRI_NAMESPACE + "property1"),
        new Literal("test string"));

    String expectedResult = "<http://example.com/class1> " +
        "<http://example.com/property1> " +
        "\"test string\"^^<http://www.w3.org/2001/XMLSchema#string> .";

    String actualResult = triplesFormatHandler.serializeTriple(triple);

    assertThat(actualResult, equalTo(expectedResult));
  }
}