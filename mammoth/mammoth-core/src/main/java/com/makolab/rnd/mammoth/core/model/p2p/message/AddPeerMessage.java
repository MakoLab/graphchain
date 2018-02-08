package com.makolab.rnd.mammoth.core.model.p2p.message;

import com.makolab.rnd.mammoth.core.util.GensonUtils;

public class AddPeerMessage extends Message {

  public String peerAddress;

  @Override
  public MessageType getMessageType() {
    return MessageType.ADD_PEER;
  }

  @Override
  public boolean isMessageValid() {
    if (peerAddress == null || peerAddress.equals("")) {
      return false;
    }

    return true;
  }

  @Override
  public String serializeAsJson() {
    return GensonUtils.createGenson().serialize(this);
  }
}
