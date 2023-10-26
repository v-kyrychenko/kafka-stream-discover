package org.vkyr.kafka.stream.discover.app.words;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;

import static org.vkyr.kafka.stream.discover.config.KafkaUtils.launchApp;

public class WordCountStreamApp {

    static final String STREAM_APP_ID = "vkyr-stream-discover";

    static final String STREAM_APP_IN = "streams-plaintext-input";
    static final String STREAM_APP_OUT = "streams-plaintext-output";

    public Topology topology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(STREAM_APP_IN);

        KTable<String, Long> wordCounts = stream
                .mapValues((readOnlyKey, value) -> value.toLowerCase())
                .flatMapValues((readOnlyKey, value) -> Arrays.asList(value.split(" ")))
                .selectKey((key, value) -> value)
                .groupByKey()
                .count(Named.as("Counts"));

        wordCounts.toStream().to(STREAM_APP_OUT, Produced.with(Serdes.String(), Serdes.Long()));

        return builder.build();
    }

    public static void main(String[] args) {
        WordCountStreamApp app = new WordCountStreamApp();
        launchApp(STREAM_APP_ID, app.topology());
    }
}