package org.vkyr.kafka.stream.discover.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;

public final class KafkaUtils {

    private static final Logger log = LoggerFactory.getLogger(KafkaUtils.class);

    public static Properties initApp(String appName) {
        Properties properties = new Properties();
        properties.put(APPLICATION_ID_CONFIG, appName);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(STATE_DIR_CONFIG, appName + UUID.randomUUID());
        return properties;
    }

    public static void launchApp(String appName, Topology topology) {
        KafkaStreams streams = new KafkaStreams(topology, initApp(appName));
        streams.start();
        log.info(topology.describe().toString());
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
