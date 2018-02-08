package com.makolab.rnd.mammoth.core.test;

import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

  public static BlockHeader prepareBlockHeader() {
    return prepareBlockHeader("1", "0");
  }

  public static BlockHeader prepareBlockHeader(String index, String previousBlockIndex) {
    return new BlockHeader(
        "http://example.com/graphIri",
        "xyz",
        "abc",
        index,
        "http://www.ontologies.makolab.com/bc/GraphChainJava/" + previousBlockIndex,
        "ppp",
        "123456"
    );
  }

  public static String readFileAsString(String relativeFilePath) {
    InputStream is = TestUtils.class.getResourceAsStream(relativeFilePath);
    try {
      return IOUtils.toString(is);
    } catch(IOException ex) {
      throw new IllegalStateException("Exception was thrown while reading text from input stream to String.", ex);
    }
  }
}
