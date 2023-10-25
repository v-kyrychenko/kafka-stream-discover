# Kafka stream discover

## 1. WordCount streaming app
Simple hello word app from official documentation

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
* Take a comma delimited topic of userId,color
* Get the running count of the favorite colours overall and output to a topic
* User's color can change

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

## 3. BankBalance streaming app
Simple bank transaction aggregation app

### 3.1. Result topic
```
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 \
 --replication-factor 1 --partitions 2 \
 --config cleanup.policy=compact \
 --topic bank-balance-output
```

### 3.2. Produce input data

Run org.vkyr.kafka.stream.discover.app.bank.balance.BankTransactionsProducer.main

### 2.3. Consume result
```
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic bank-balance-output \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```