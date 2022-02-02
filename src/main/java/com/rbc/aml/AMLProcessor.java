package com.rbc.aml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AMLProcessor {
    public static void main(String[] args) {
        if (args.length<1){
            System.out.println("usage AMLProcessor filename");
            System.exit(0);
        }
        String fileName = args[0];
        AMLProcessor amlProcessor = new AMLProcessor();
        amlProcessor.processFile(fileName);
    }

    private void processFile(String fileName) {

        TransactionStore store = new TransactionStore();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            String line;
                while ((line = bufferedReader.readLine())!=null){
                    String []cols = line.split((","));
                    if (cols.length!=3){
                        System.out.println("invalid row "+ line);
                    }else {
                        Transaction transaction = new Transaction(cols[0].trim(), cols[1].trim(), cols[2].trim());
                        boolean valid = store.addAndValidate(transaction);
                        if (!valid) {
                            System.out.println( transaction + " failed aml check because of existing transactions " + store.getCurrentTransactions(transaction.getAccount()) );
                        } else {
                            System.out.println("transaction processed successfully" + transaction );
                        }
                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
