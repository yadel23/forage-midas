package com.jpmc.midascore.entity;

import com.jpmc.midascore.foundation.Transaction;

import jakarta.persistence.*;

@Entity
public class TransactionRecord extends Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long senderId;

    @Column
    private long recipientId;

    @Column
    private float transactionAmount;

    @Column
    private float senderBalance;

    @Column
    private float recipientBalance;

    protected TransactionRecord() {
    }

    public TransactionRecord(long senderId, long recipientId, float transactionAmount) {
        super(senderId, recipientId, transactionAmount);
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.transactionAmount = transactionAmount;
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public float getSenderBalance() {
        return senderBalance;
    }

    public void setSenderBalance(float senderBalance) {
        this.senderBalance = senderBalance;
    }

    public float getRecipientBalance() {
        return recipientBalance;
    }

    public void setRecipientBalance(float recipientBalance) {
        this.recipientBalance = recipientBalance;
    }
}
