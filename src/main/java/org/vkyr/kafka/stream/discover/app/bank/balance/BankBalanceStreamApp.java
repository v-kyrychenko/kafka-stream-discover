package org.vkyr.kafka.stream.discover.app.bank.balance;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.vkyr.kafka.stream.discover.app.bank.balance.model.UserTrx;

import java.math.BigDecimal;
import java.time.Instant;

import static org.vkyr.kafka.stream.discover.app.bank.balance.Constants.STREAM_APP_ID;
import static org.vkyr.kafka.stream.discover.app.bank.balance.Constants.STREAM_APP_IN;
import static org.vkyr.kafka.stream.discover.app.bank.balance.Constants.STREAM_APP_OUT;
import static org.vkyr.kafka.stream.discover.config.KafkaUtils.launchApp;
import static org.vkyr.kafka.stream.discover.config.KafkaUtils.toDto;
import static org.vkyr.kafka.stream.discover.config.KafkaUtils.toJsonString;

public class BankBalanceStreamApp {

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> inStream = builder.stream(STREAM_APP_IN);

        inStream
                .groupByKey()
                .reduce(BankBalanceStreamApp::accumulateBalance)
                .toStream()
                .to(STREAM_APP_OUT);

        launchApp(STREAM_APP_ID, builder.build());
    }

    private static String accumulateBalance(String trx1Msg, String trx2Msg) {
        UserTrx trx1 = toDto(trx1Msg, UserTrx.class);
        UserTrx trx2 = toDto(trx2Msg, UserTrx.class);

        BigDecimal balance = trx1.getAmount().add(trx2.getAmount());
        Instant lastUpdate = Instant.ofEpochMilli(Math.max(
                trx1.getTime().toEpochMilli(),
                trx2.getTime().toEpochMilli()));
        return toJsonString(new UserTrx(trx1.getUserId(), trx1.getName(), balance, lastUpdate));
    }
}
