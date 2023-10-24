# Kafka stream discover

## 1. WordCount streaming app

### 1.1. In topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --topic streams-plaintext-input
```

### 1.2. Out topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --topic streams-plaintext-output
```

### 1.3. Produce input data
```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic streams-plaintext-input
```

### 1.4. Consume result
```
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-plaintext-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```

## 2. Favorite color streaming app
### 2.1. In topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --topic streams-colors-input
```

### 2.2. In the middle topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --config cleanup.policy=compact \
 --topic streams-colors-middle
```

### 2.2. Out topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --config cleanup.policy=compact \
 --topic streams-colors-output
```

### 2.3. Produce input data
```
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic streams-colors-input
```

### 2.4. Consume result
```
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic streams-colors-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```