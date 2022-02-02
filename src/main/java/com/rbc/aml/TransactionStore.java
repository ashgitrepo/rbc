package com.rbc.aml;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class TransactionStore {

    private static final int SECONDS_60 = 60;
    private static final BigDecimal AML_THRESHHOLD = new BigDecimal(50000);
    /**
     * maintains a map of account to the sum of transactions within the last minute;
     */
    private Map<String, BigDecimal> transactionsSum = new HashMap<>();
    private Map<String, PriorityQueue<Transaction>> transactions = new HashMap<>();

    public boolean addAndValidate(Transaction transaction){

        boolean valid = true;
        PriorityQueue<Transaction> pq ;
        if (!transactionsSum.containsKey(transaction.getAccount())){
             pq = new PriorityQueue<>(Comparator.comparing(Transaction::getTxTime));
            transactions.put(transaction.getAccount(), pq);
        }else {
            // poll from queue as long as the current transaction and root of priority queue is more than 60 seconds apart
            pq = transactions.get(transaction.getAccount());

            while (!pq.isEmpty()){
                Transaction prevTransaction = pq.peek();
               if (Duration.between(prevTransaction.getTxTime(), transaction.getTxTime()).toSeconds() >SECONDS_60){
                   pq.poll();
                   BigDecimal prevSum = transactionsSum.get(transaction.getAccount());
                   transactionsSum.put(transaction.getAccount(), prevSum.subtract(prevTransaction.getAmount()).abs());
               }else {
                   break;
               }
            }
        }
        BigDecimal prevSum = transactionsSum.getOrDefault(transaction.getAccount(), BigDecimal.ZERO);
        transactionsSum.put(transaction.getAccount(),prevSum.add(transaction.getAmount())); // update the balance
        pq.offer(transaction); // add te new transaction to pq
        if (prevSum.add(transaction.getAmount()).compareTo(AML_THRESHHOLD)>0){
            valid = false;
        }
        return  valid;

    }
    public List<Transaction> getCurrentTransactions(String account){
        return new ArrayList<>(transactions.get(account));
    }

}
