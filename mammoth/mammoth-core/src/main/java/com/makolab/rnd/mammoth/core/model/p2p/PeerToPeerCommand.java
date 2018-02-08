package com.makolab.rnd.mammoth.core.model.p2p;

import com.makolab.rnd.mammoth.core.model.p2p.message.Message;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;

public class PeerToPeerCommand {

  public CommandType commandType;
  public Message message;

  public PeerToPeerCommand(CommandType commandType) {
    this.commandType = commandType;
  }

  public PeerToPeerCommand(CommandType commandType, Message message) {
    this.commandType = commandType;
    this.message = message;
  }

  public MessageType getMessageType() {
    if (message != null) {
      return message.getMessageType();
    } else {
      return null;
    }
  }

  @Override
  public String toString() {
    return "PeerToPeerCommand{" +
        "commandType=" + commandType +
        ", message=" + message +
        '}';
  }
}
