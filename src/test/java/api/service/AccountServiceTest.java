package api.service;

import api.dto.AccountDTO;
import api.exception.AccountNotFoundException;
import api.exception.NotEnoughBalanceException;
import api.repository.AccountRepository;
import api.util.DtoUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccountServiceTest {
    private AccountService accountService;
    AccountRepository accountRepository;

    @Before
    public void setup() {
        if(accountRepository == null) {
            accountRepository = new AccountRepository();
        }

        if(accountService == null) {
            accountService = new AccountService(accountRepository);
        }
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool, Long  startTime) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            } else {
                try {
                    AccountDTO account_11 = DtoUtils.toAccountDto(accountRepository.getAccountByAccountNumber(10000011L));
                    AccountDTO account_22 = DtoUtils.toAccountDto(accountRepository.getAccountByAccountNumber(10000022L));

                    System.out.println("Bal 11 : " + account_11.getBalanceAmount() + ", Bal 22 : " + account_22.getBalanceAmount());
                    System.out.println("Time took to complete the test : " + (System.currentTimeMillis() - startTime) + " ms");
                } catch (AccountNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException ex) {
        threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testMoneyTransferWithMultipleThreads() {
        long startTime = System.currentTimeMillis();

        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.submit(() -> {
            try {
                System.out.println("11 -> 22 : 200");
                accountService.transferMoney(10000011L, 10000022L, new BigDecimal("200"), "11 -> 22 : 200");
                //accountService.withdrawMoney(10000011L, new BigDecimal("200"));
                //accountService.depositMoney(10000022L, new BigDecimal("200"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                System.out.println("11 -> 22 : 500");
                accountService.transferMoney(10000011L, 10000022L, new BigDecimal("500"), "11 -> 22 : 500");
                //accountService.withdrawMoney(10000011L, new BigDecimal("500"));
                //accountService.depositMoney(10000022L, new BigDecimal("500"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                System.out.println("22 -> 11 : 300");
                accountService.transferMoney(10000022L, 10000011L, new BigDecimal("300"), "22 -> 11 : 300");
                //accountService.withdrawMoney(10000022L, new BigDecimal("200"));
                //accountService.depositMoney(10000011L, new BigDecimal("200"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                System.out.println("22 -> 11 : 250");
                accountService.transferMoney(10000022L, 10000011L, new BigDecimal("250"), "22 -> 11 : 250");
                //accountService.withdrawMoney(10000022L, new BigDecimal("250"));
                //accountService.depositMoney(10000011L, new BigDecimal("250"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });

        executor.submit(() -> {
            try {
                System.out.println("22 -> 11 : 350");
                accountService.transferMoney(10000022L, 10000011L, new BigDecimal("350"), "22 -> 11 : 350");
                //accountService.withdrawMoney(10000022L, new BigDecimal("350"));
                //accountService.depositMoney(10000011L, new BigDecimal("350"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                System.out.println("11 -> 22 : 150");
                accountService.transferMoney(10000011L, 10000022L, new BigDecimal("150"), "11 -> 22 : 150");
                //accountService.withdrawMoney(10000011L, new BigDecimal("150"));
                //accountService.depositMoney(10000022L, new BigDecimal("150"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                System.out.println("22 -> 11 : 450");
                accountService.transferMoney(10000022L, 10000011L, new BigDecimal("450"), "22 -> 11 : 450");
                //accountService.withdrawMoney(10000022L, new BigDecimal("450"));
                //accountService.depositMoney(10000011L, new BigDecimal("450"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                System.out.println("22 -> 11 : 350");
                accountService.transferMoney(10000022L, 10000011L, new BigDecimal("350"), "22 -> 11 : 350");
                //accountService.withdrawMoney(10000022L, new BigDecimal("350"));
                //accountService.depositMoney(10000011L, new BigDecimal("350"));
            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            } catch (NotEnoughBalanceException e) {
                e.printStackTrace();
            }
        });
        awaitTerminationAfterShutdown(executor, startTime);
   }
}
