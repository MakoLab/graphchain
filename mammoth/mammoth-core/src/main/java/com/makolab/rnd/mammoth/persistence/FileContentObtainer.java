package com.makolab.rnd.mammoth.persistence;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class FileContentObtainer {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileContentObtainer.class);

  public Optional<String> getFileContent(String filePath) {
    try (InputStream is = getClass().getResourceAsStream(filePath)) {
      return Optional.of(IOUtils.toString(is, StandardCharsets.UTF_8));
    } catch (IOException ex) {
      LOGGER.warn("Exception was thrown while reading query template from a file.", ex);
    }

    return Optional.empty();
  }
}
