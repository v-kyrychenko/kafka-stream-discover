package org.vkyr.kafka.stream.discover.app.color;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;

import static org.vkyr.kafka.stream.discover.config.KafkaUtils.launchApp;

public class FavoriteColorStreamApp {

    private static final String STREAM_APP_ID = "vkyr-stream-colors";

    private static final String STREAM_APP_IN = "streams-colors-input";
    private static final String STREAM_APP_MIDDLE = "streams-colors-middle";
    private static final String STREAM_APP_OUT = "streams-colors-output";

    public Topology topology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> inStream = builder.stream(STREAM_APP_IN);

        inStream.filter((key, value) -> value != null && value.contains(","))
                .selectKey((key, value) -> value.split(",")[0])
                .mapValues((readOnlyUserName, value) -> value.split(",")[1])
                .to(STREAM_APP_MIDDLE);

        KTable<String, String> middleStream = builder.table(STREAM_APP_MIDDLE);
        KTable<String, Long> colorCountStream = middleStream
                .groupBy((userName, color) -> KeyValue.pair(color, color))
                .count(Named.as("CountsByColor"));

        colorCountStream
                .toStream()
                .to(STREAM_APP_OUT, Produced.with(Serdes.String(), Serdes.Long()));

        return builder.build();
    }

    public static void main(String[] args) {
        FavoriteColorStreamApp app = new FavoriteColorStreamApp();
        launchApp(STREAM_APP_ID, app.topology());
    }
}