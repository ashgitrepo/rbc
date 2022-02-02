package com.rbc.aml;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Transaction {

    private String account;
    private BigDecimal amount;
    private LocalTime txTime;

    public Transaction (String txTime, String amount, String account ){
        this.account = account;
        this.amount = new BigDecimal(amount).abs();
        String times[] = txTime.split(":");
        this.txTime = LocalTime.of(Integer.parseInt(times[0]),
                Integer.parseInt(times[1]),
                Integer.parseInt(times[2]));
    }

    public String getAccount() {
        return account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalTime getTxTime() {
        return txTime;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "account='" + account + '\'' +
                ", amount=" + amount +
                ", txTime=" + txTime +
                '}';
    }
}
