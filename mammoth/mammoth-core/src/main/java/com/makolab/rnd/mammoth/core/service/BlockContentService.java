package com.makolab.rnd.mammoth.core.service;

import com.makolab.rnd.mammoth.core.model.chain.BlockContent;
import com.makolab.rnd.mammoth.core.ramt.Rdf4jMapper;
import com.makolab.rnd.mammoth.core.service.base64.Base64Handler;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.stereotype.Service;
import javax.inject.Inject;

@Service
public class BlockContentService {

  private Base64Handler base64Handler;

  private Rdf4jMapper rdf4jMapper;

  @Inject
  public BlockContentService(Base64Handler base64Handler) {
    this.base64Handler = base64Handler;

    this.rdf4jMapper = new Rdf4jMapper();
  }

  public String calculateBase64(BlockContent blockContent) {
    String serializedModel = rdf4jMapper.triplesToSerialization(blockContent.getTriples(), RDFFormat.TURTLE);
    return base64Handler.toBase64(serializedModel);
  }
}
