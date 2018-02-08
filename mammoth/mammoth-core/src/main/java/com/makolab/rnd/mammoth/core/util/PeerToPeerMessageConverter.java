package com.makolab.rnd.mammoth.core.util;

import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class PeerToPeerMessageConverter implements Converter<PeerToPeerMessage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerToPeerMessageConverter.class);

  @Override
  public void serialize(PeerToPeerMessage object, ObjectWriter objectWriter, Context ctx) throws Exception {
    objectWriter.beginObject();
    objectWriter.writeNumber("type", object.messageType.ordinal());
    objectWriter.writeName("data").beginArray();
    for (BlockHeader blockHeader : object.data) {
      objectWriter.beginObject()
          .writeString("dataGraphIri", blockHeader.getDataGraphIri())
          .writeString("dataHash", blockHeader.getDataHash())
          .writeString("hash", blockHeader.getHash())
          .writeString("index", blockHeader.getIndex())
          .writeString("previousBlock", blockHeader.getPreviousBlock())
          .writeString("previousHash", blockHeader.getPreviousHash())
          .writeString("timestamp", blockHeader.getTimestamp())
          .endObject();
    }
    objectWriter.endArray();
    objectWriter.writeName("graph").beginObject();
    for (PeerToPeerMessage.GraphData graphData : object.graph) {
      objectWriter.writeString(graphData.graphIri, graphData.graphContent);
    }
    objectWriter.endObject(); // graph
    objectWriter.endObject(); // [root]
    objectWriter.flush();
  }

  @Override
  public PeerToPeerMessage deserialize(ObjectReader reader, Context ctx) throws Exception {
    PeerToPeerMessage message = new PeerToPeerMessage();
    reader.beginObject();

    while (reader.hasNext()) {
      reader.next();
      if ("type".equals(reader.name())) {
        message.messageType = MessageType.fromOrdinal(reader.valueAsInt());
      } else if ("data".equals(reader.name())) {
        message.data = handleData(reader);
      } else if ("graph".equals(reader.name())) {
        message.graph = handleGraph(reader);
      } else {
        LOGGER.debug("Unknown element of parsing JSON: '{}'.", reader.name());
      }
    }

    reader.endObject();
    return message;
  }

  private List<BlockHeader> handleData(ObjectReader reader) {
    List<BlockHeader> result = new ArrayList<>();

    reader.beginArray();
    while (reader.hasNext()) {
      reader.next();

      reader.beginObject();

      String dataGraphIri = null;
      String dataHash = null;
      String hash = null;
      String index = null;
      String previousBlock = null;
      String previousHash = null;
      String timestamp = null;

      while (reader.hasNext()) {
        reader.next();
        switch (reader.name()) {
          case "dataGraphIri":
            dataGraphIri = reader.valueAsString();
            break;
          case "dataHash":
            dataHash = reader.valueAsString();
            break;
          case "hash":
            hash = reader.valueAsString();
            break;
          case "index":
            index = reader.valueAsString();
            break;
          case "previousBlock":
            previousBlock = reader.valueAsString();
            break;
          case "previousHash":
            previousHash = reader.valueAsString();
            break;
          case "timestamp":
            timestamp = reader.valueAsString();
            break;
          default:
            LOGGER.debug("Unknown name '{}' for block header object.", reader.name());
            reader.skipValue();
        }
      }
      reader.endObject();

      result.add(new BlockHeader(dataGraphIri, dataHash, hash, index, previousBlock, previousHash, timestamp));
    }
    reader.endArray();

    return result;
  }

  private List<PeerToPeerMessage.GraphData> handleGraph(ObjectReader reader) {
    List<PeerToPeerMessage.GraphData> result = new ArrayList<>();

    reader.beginObject();
    while (reader.hasNext()) {
      reader.next();

      String graphIri = reader.name();
      String graphContent = reader.valueAsString();

      result.add(new PeerToPeerMessage.GraphData(graphIri, graphContent));
    }
    reader.endObject();

    return result;
  }
}