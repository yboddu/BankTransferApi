package api.model;

import api.exception.NotEnoughBalanceException;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class Account {
    private Long accountNumber;
    private BigDecimal balanceAmount;
    private String currencyCode;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    public Account(Long accountNumber, BigDecimal initialBalance, String currencyCode) {
        this.accountNumber = accountNumber;
        this.balanceAmount = initialBalance;
        this.currencyCode = currencyCode;
    }

    public void withdrawFunds(BigDecimal withdrawalAmount) {
        Function<BigDecimal, BigDecimal> withDrawFunction = parameter -> {
            if(this.balanceAmount.compareTo(withdrawalAmount) == -1) {
                throw new NotEnoughBalanceException(String.format("Account with id: %s does not have enough funds to withdraw : %s", accountNumber, withdrawalAmount));
            }
            return this.balanceAmount.subtract(withdrawalAmount);
        };
        executeWithLock(withdrawalAmount, withDrawFunction);
    }

    public void depositFunds(BigDecimal depositAmount) {
        Function<BigDecimal, BigDecimal> depositFunction = parameter -> this.balanceAmount.add(depositAmount);
        executeWithLock(depositAmount, depositFunction);
    }

    private void executeWithLock(BigDecimal amount, Function<BigDecimal, BigDecimal> withdrawOrDepostFunction){
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();

        try {
            this.balanceAmount = withdrawOrDepostFunction.apply(amount);
        } finally {
          writeLock.unlock();
        }
    }

    //Getters
    public Long getAccountNumber() {
        return this.accountNumber;
    }

    public BigDecimal getBalanceAmount() {
        Lock readLock = this.lock.readLock();
        readLock.lock();

        try {
            return this.balanceAmount;
        } finally {
            readLock.unlock();
        }
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }
}
