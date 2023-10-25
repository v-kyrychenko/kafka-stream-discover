package org.vkyr.kafka.stream.discover.app.bank.balance.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;

public class UserTrx {

    private String trxId;

    private String userId;

    private String name;

    private BigDecimal amount;

    private Instant time;

    public UserTrx() {
    }

    public UserTrx(String userId, String name, BigDecimal amount, Instant time) {
        this.trxId = UUID.randomUUID().toString();
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.time = time;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserTrx.class.getSimpleName() + "[", "]")
                .add("trxId='" + trxId + "'")
                .add("userId='" + userId + "'")
                .add("amount=" + amount)
                .toString();
    }
}
