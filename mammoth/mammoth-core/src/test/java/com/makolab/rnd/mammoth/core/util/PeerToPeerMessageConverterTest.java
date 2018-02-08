package com.makolab.rnd.mammoth.core.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import com.makolab.rnd.mammoth.core.model.chain.BlockHeader;
import com.makolab.rnd.mammoth.core.model.p2p.message.Message;
import com.makolab.rnd.mammoth.core.model.p2p.message.MessageType;
import com.makolab.rnd.mammoth.core.model.p2p.message.PeerToPeerMessage;
import com.makolab.rnd.mammoth.core.test.TestUtils;
import com.owlike.genson.Genson;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeerToPeerMessageConverterTest {

  @Test
  public void shouldReturnSerializedMessageWhenDataAndGraphAreEmpty() {
    Message message = new PeerToPeerMessage(
        MessageType.QUERY_LATEST
    );

    String expectedResult = TestUtils.readFileAsString("/peer_to_peer_messages/message0.json");

    String actualResult = createGenson().serialize(message);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnSerializedMessageWhenDataIsPresent() {
    Message message = new PeerToPeerMessage(
        MessageType.QUERY_ALL,
        Arrays.asList(
            TestUtils.prepareBlockHeader("1", "0"),
            TestUtils.prepareBlockHeader("2", "1")
        ),
        new ArrayList<>()
    );

    String expectedResult = TestUtils.readFileAsString("/peer_to_peer_messages/message1.json");

    String actualResult = createGenson().serialize(message);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnSerializedMessageWhenGraphIsPresent() {
    Message message = new PeerToPeerMessage(
        MessageType.QUERY_ALL,
        new ArrayList<>(),
        Arrays.asList(
            new PeerToPeerMessage.GraphData("http://example.com/graphIri1", "base64Content"),
            new PeerToPeerMessage.GraphData("http://example.com/graphIri2", "base64Content")
        )
    );

    String expectedResult = TestUtils.readFileAsString("/peer_to_peer_messages/message2.json");

    String actualResult = createGenson().serialize(message);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnMessageDeserializedAccordinglyWhenDataAndGraphAreEmpty() {
    String serializedMessage = TestUtils.readFileAsString("/peer_to_peer_messages/message0.json");

    PeerToPeerMessage expectedResult = new PeerToPeerMessage(MessageType.QUERY_LATEST);

    PeerToPeerMessage actualResult = createGenson().deserialize(serializedMessage, PeerToPeerMessage.class);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnMessageDeserializedAccordinglyWhenDataIsPresent() {
    String serializedMessage = TestUtils.readFileAsString("/peer_to_peer_messages/message1.json");

    List<BlockHeader> data = Arrays.asList(
        TestUtils.prepareBlockHeader("1", "0"),
        TestUtils.prepareBlockHeader("2", "1")
    );
    PeerToPeerMessage expectedResult = new PeerToPeerMessage(MessageType.QUERY_ALL, data, new ArrayList<>());

    PeerToPeerMessage actualResult = createGenson().deserialize(serializedMessage, PeerToPeerMessage.class);

    assertThat(actualResult, equalTo(expectedResult));
  }

  @Test
  public void shouldReturnMessageDeserializedAccordinglyWhenGraphIsPresent() {
    String serializedMessage = TestUtils.readFileAsString("/peer_to_peer_messages/message2.json");

    List<PeerToPeerMessage.GraphData> graph = Arrays.asList(
        new PeerToPeerMessage.GraphData("http://example.com/graphIri1", "base64Content"),
        new PeerToPeerMessage.GraphData("http://example.com/graphIri2", "base64Content")
    );
    PeerToPeerMessage expectedResult = new PeerToPeerMessage(MessageType.QUERY_ALL, new ArrayList<>(), graph);

    PeerToPeerMessage actualResult = createGenson().deserialize(serializedMessage, PeerToPeerMessage.class);

    assertThat(actualResult, equalTo(expectedResult));
  }

  private Genson createGenson() {
    return GensonUtils.createGensonWithConverters();
  }
}