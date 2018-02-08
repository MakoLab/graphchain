package com.makolab.rnd.mammoth.core.service.base64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class Base64HandlerTest {

  private Base64Handler base64Handler = new Base64Handler();

  @Test
  public void shouldReturnStringForBase64DecodedString() {
    String base64Data = "R3JhcGhDaGFpbg==";

    String expectedResult = "GraphChain";

    String actualResult = base64Handler.toString(base64Data);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnBase64StringForRawString() {
    String rawString = "zażółć gęślą jaźń";

    String expectedResult = "emHFvMOzxYLEhyBnxJnFm2zEhSBqYcW6xYQ=";

    String actualResult = base64Handler.toBase64(rawString);

    assertThat(actualResult, equalTo(expectedResult));
  }
}