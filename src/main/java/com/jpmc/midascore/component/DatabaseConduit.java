package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        System.out.println("Transaction to process: " + transaction.toString());
        System.out.println("Trying to insert to Transaction Record Table");

        UserRecord senderInfo = userRepository.findById(transaction.getSenderId());
        UserRecord recipientInfo = userRepository.findById(transaction.getRecipientId());

        if (senderInfo != null && recipientInfo != null && senderInfo.getBalance() >= transaction.getAmount()){
            TransactionRecord record = new TransactionRecord(
                    transaction.getSenderId(),
                    transaction.getRecipientId(),
                    transaction.getAmount()
            );
            float recipientBalance = recipientInfo.getBalance();
            float senderBalance = senderInfo.getBalance();

            record.setRecipientBalance(recipientBalance + transaction.getAmount());
            record.setSenderBalance(senderBalance - transaction.getAmount());
            System.out.println("trying to send: " + record.toString());

            System.out.println("info: " + senderBalance + " " + recipientBalance);
            transactionRepository.save(record);
            senderInfo.setBalance(senderBalance - transaction.getAmount());
            recipientInfo.setBalance(recipientBalance + transaction.getAmount());
            this.save(senderInfo);
            this.save(recipientInfo);
        }
    }
}
