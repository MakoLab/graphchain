package com.makolab.rnd.mammoth.core.service.cryptography;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class Sha256HashCalculatorTest {

  private Sha256HashCalculator hashCalculator = new Sha256HashCalculator();

  @Test
  public void shouldReturnProperHashAsStringForSpecificInput() {
    String input = "GraphChain";

    String expectedResult = "64db22876fc25365e482c0fb09dc51109b3a91c0865323582709f4164eac8c0f";

    String actualResult = hashCalculator.calculateHash(input);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnProperHashAsStringForSpecificInputWithPolishChars() {
    String input = "Zażółć gęślą jaźń";

    String expectedResult = "bc5348fd7c2dd8bbf411f0b9268265f7c2e0d31ebf314695882b8170c7e1e9d7";

    String actualResult = hashCalculator.calculateHash(input);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnProperHashAsStringForUrl() {
    String input = "http://ontologies.makolab.com/gc/";

    String expectedResult = "4613be6647372e30fced2fc1899bd035b8192a720112c939df5141bd01c78d30";

    String actualResult = hashCalculator.calculateHash(input);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnProperHashAsStringForNTriple() {
    String serializedTriple = "<http://example.com/class1> " +
        "<http://example.com/property1> " +
        "\"test string\"^^<http://www.w3.org/2001/XMLSchema#string> .";

    String expectedResult = "02d797818a7ee7f4e303941acba57adae8ba85fd82399d03e94af2d60163399b";

    String actualResult = hashCalculator.calculateHash(serializedTriple);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnProperHashForNTriplesSerializationOfTriple() {
    String serializedTriple = "<http://ontologies.makolab.com/gc/> " +
        "<http://ontologies.makolab.com/gc/name> " +
        "\"Ontology\"^^<http://www.w3.org/2001/XMLSchema#string> .";

    String expectedResult = "9ca7f0185c00422b045dc8256a4872ef713ac57b059f49d1ad78808d4aca3c6a";

    String actualResult = hashCalculator.calculateHash(serializedTriple);

    assertThat(actualResult, equalTo(expectedResult));
  }
}