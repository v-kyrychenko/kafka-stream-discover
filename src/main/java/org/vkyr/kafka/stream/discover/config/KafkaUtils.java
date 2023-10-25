package org.vkyr.kafka.stream.discover.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.PROCESSING_GUARANTEE_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;

public final class KafkaUtils {

    private static final Logger log = LoggerFactory.getLogger(KafkaUtils.class);

    public static final ObjectMapper MAPPER = getObjectMapper();

    public static Properties initConnection() {
        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }

    public static Properties initAppProducer() {
        Properties props = new Properties();
        props.putAll(initConnection());

        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, "3");
        props.put(ENABLE_IDEMPOTENCE_CONFIG, "true");
        return props;
    }

    public static Properties initApp(String appName) {
        Properties properties = new Properties();
        properties.putAll(initConnection());
        properties.put(APPLICATION_ID_CONFIG, appName);
        properties.put(STATE_DIR_CONFIG, appName + UUID.randomUUID());
        properties.put(PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        return properties;
    }

    public static void launchApp(String appName, Topology topology) {
        KafkaStreams streams = new KafkaStreams(topology, initApp(appName));
        streams.start();
        log.info(topology.describe().toString());
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    public static <T> T toDto(String value, Class<T> tClass) {
        try {
            return MAPPER.readValue(value, tClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can't read message:" + value);
        }
    }

    public static <T> String toJsonString(T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can't convert message:" + object);
        }
    }
}
