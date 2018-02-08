package com.makolab.rnd.mammoth.core.model.p2p.message;

public abstract class Message {

  public abstract MessageType getMessageType();

  public abstract boolean isMessageValid();

  public abstract String serializeAsJson();
}
