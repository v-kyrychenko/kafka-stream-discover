package org.vkyr.kafka.stream.discover.app.bank.balance;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vkyr.kafka.stream.discover.app.bank.balance.model.UserTrx;
import org.vkyr.kafka.stream.discover.config.KafkaUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import static org.vkyr.kafka.stream.discover.app.bank.balance.Constants.STREAM_APP_IN;
import static org.vkyr.kafka.stream.discover.config.KafkaUtils.initAppProducer;

public class BankTransactionsProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaUtils.class);
    private static final int MESSAGES_TOTAL = 1000;

    public static void main(String[] args) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(initAppProducer())) {
            produceMessages(producer);
        } catch (InterruptedException e) {
            log.error("Can't produce message", e);
        }
    }

    private static void produceMessages(Producer<String, String> producer) throws InterruptedException {
        int messageCounter = 0;

        do {
            UserTrx userTrx = buildTrx();
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(STREAM_APP_IN, userTrx.getUserId(), KafkaUtils.toJsonString(userTrx));
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Error sending message: {}", exception.getMessage());
                }
            });

            log.info("Sent: " + userTrx);
            messageCounter++;
        } while (MESSAGES_TOTAL >= messageCounter);

        log.info("Total messages sent:{}", messageCounter);
    }

    private static UserTrx buildTrx() {
        String userId = generateUserId();
        return new UserTrx(userId, "User" + userId, generateAmount(), Instant.now());
    }

    private static String generateUserId() {
        return String.valueOf((long) ((Math.random() * (11 - 1)) + 1));
    }

    private static BigDecimal generateAmount() {
        double amount = (Math.random() * (100 - 1));
        return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }
}
