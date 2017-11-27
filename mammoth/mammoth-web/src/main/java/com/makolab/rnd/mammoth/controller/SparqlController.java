package com.makolab.rnd.mammoth.controller;

import com.makolab.rnd.mammoth.persistence.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.inject.Inject;

@Controller
@RequestMapping("sparql")
public class SparqlController {

  private final Logger LOGGER = LoggerFactory.getLogger(SparqlController.class);

  @Inject
  private RepositoryManager repositoryManager;

  @RequestMapping(
      value = "/query",
      method = RequestMethod.POST
  )
  public ResponseEntity<String> makeQuery(@RequestBody String sparqlQuery) {
    LOGGER.debug("[POST] sparql/query");
    LOGGER.trace("<sparqlQuery>\n{}\n</sparqlQuery>", sparqlQuery);

    String serializedQueryResult = repositoryManager.makeSparqlQuery(sparqlQuery);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/ld+json"));

    return new ResponseEntity<>(serializedQueryResult, headers, HttpStatus.OK);
  }
}
