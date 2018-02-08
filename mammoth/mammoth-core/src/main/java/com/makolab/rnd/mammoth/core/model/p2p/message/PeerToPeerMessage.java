package com.makolab.rnd.mammoth.core.model.p2p.message;

import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.util.GensonUtils;
import com.owlike.genson.Genson;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PeerToPeerMessage extends Message {

  public MessageType messageType;
  public List<BlockHeader> data;
  public List<GraphData> graph;

  public PeerToPeerMessage() {
  }

  public PeerToPeerMessage(MessageType messageType) {
    this.messageType = messageType;
    this.data = new ArrayList<>();
    this.graph = new ArrayList<>();
  }

  public PeerToPeerMessage(MessageType messageType, List<BlockHeader> data, List<GraphData> graph) {
    this.messageType = messageType;
    this.data = data;
    this.graph = graph;
  }

  @Override
  public MessageType getMessageType() {
    return messageType;
  }

  @Override
  public boolean isMessageValid() {
    if (messageType == null) {
      return false;
    }
    if (data == null) {
      return false;
    }
    // TODO: GraphData

    return true;
  }

  @Override
  public String serializeAsJson() {
    Genson genson = GensonUtils.createGensonWithConverters();
    return genson.serialize(this);
  }

  @Override
  public String toString() {
    return "PeerToPeerMessage{" +
        "messageType=" + messageType +
        ", data=" + data +
        ", graph=" + graph +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeerToPeerMessage message = (PeerToPeerMessage) o;
    return messageType == message.messageType &&
        Objects.equals(data, message.data) &&
        Objects.equals(graph, message.graph);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageType, data, graph);
  }

  public static class GraphData {
    public String graphIri;
    public String graphContent;

    public GraphData(String graphIri, String graphContent) {
      this.graphIri = graphIri;
      this.graphContent = graphContent;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("GraphData{")
          .append("graphIri='").append(graphIri).append("'");
      if (graphContent != null && graphContent.length() > 100) {
        sb.append(", graphContent(first 100 chars)='").append(graphContent.substring(0, 100)).append("'");
      }
      sb.append("}");
      return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      GraphData graphData = (GraphData) o;
      return Objects.equals(graphIri, graphData.graphIri) &&
          Objects.equals(graphContent, graphData.graphContent);
    }

    @Override
    public int hashCode() {
      return Objects.hash(graphIri, graphContent);
    }
  }
}
