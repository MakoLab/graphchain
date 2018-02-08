package com.makolab.rnd.mammoth.core.model.p2p.message;

public enum MessageType {
  QUERY_LATEST,
  QUERY_ALL,
  RESPONSE_BLOCKCHAIN,
  ERROR,
  ADD_PEER;

  public static MessageType fromOrdinal(int messageTypeAsInt) {
    for (MessageType messageType : values()) {
      if (messageType.ordinal() == messageTypeAsInt) {
        return messageType;
      }
    }

    String msg = String.format("Message type with '%s' was not found.", messageTypeAsInt);
    throw new IllegalArgumentException(msg);
  }
}