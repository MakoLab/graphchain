package com.makolab.rnd.mammoth.core.model.p2p.message;

import com.makolab.rnd.mammoth.core.util.GensonUtils;

public class ErrorMessage extends Message {

  public final MessageType messageType = MessageType.ERROR;
  public String errorDescription;

  public ErrorMessage(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  @Override
  public MessageType getMessageType() {
    return messageType;
  }

  @Override
  public boolean isMessageValid() {
    if (errorDescription == null) {
      return false;
    }

    return true;
  }

  @Override
  public String serializeAsJson() {
    return GensonUtils.createGenson().serialize(this);
  }
}
