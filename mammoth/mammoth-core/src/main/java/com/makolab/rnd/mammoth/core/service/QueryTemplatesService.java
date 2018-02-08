package com.makolab.rnd.mammoth.core.service;

import com.makolab.rnd.mammoth.core.persistence.FileContentObtainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.inject.Inject;

@Service
public class QueryTemplatesService {

  private static Logger LOGGER = LoggerFactory.getLogger(QueryTemplatesService.class);

  @Inject
  private FileContentObtainer fileContentObtainer;

  public Optional<String> getQueryTemplate(String queryName) {
    LOGGER.trace("Trying to get a query template with name '{}'.", queryName);
    String filePath = "/query_templates/" + queryName + ".sparql";
    return fileContentObtainer.getFileContent(filePath);
  }
}