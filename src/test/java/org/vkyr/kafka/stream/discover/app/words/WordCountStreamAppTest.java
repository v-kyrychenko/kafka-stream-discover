package org.vkyr.kafka.stream.discover.app.words;

import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vkyr.kafka.stream.discover.config.KafkaUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.vkyr.kafka.stream.discover.app.words.WordCountStreamApp.STREAM_APP_IN;
import static org.vkyr.kafka.stream.discover.app.words.WordCountStreamApp.STREAM_APP_OUT;

public class WordCountStreamAppTest {

    TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, Long> outputTopic;

    @BeforeEach
    public void setup() {
        testDriver = new TopologyTestDriver(
                new WordCountStreamApp().topology(),
                KafkaUtils.initApp(WordCountStreamApp.STREAM_APP_ID));
        inputTopic = testDriver.createInputTopic(STREAM_APP_IN, new StringSerializer(), new StringSerializer());
        outputTopic = testDriver.createOutputTopic(STREAM_APP_OUT, new StringDeserializer(), new LongDeserializer());
    }

    @AfterEach
    public void closeDriver() {
        testDriver.close();
    }

    @Test
    public void testCounts() {
        inputTopic.pipeInput("test word test");

        List<KeyValue<String, Long>> result = outputTopic.readKeyValuesToList();
        assertEquals(3, result.size());
        assertIterableEquals(List.of(
                        KeyValue.pair("test", 1L),
                        KeyValue.pair("word", 1L),
                        KeyValue.pair("test", 2L)),
                result);
    }
}
