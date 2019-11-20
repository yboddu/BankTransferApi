package api.service;

import api.dto.AccountDTO;
import api.dto.MoneyTransferDTO;
import api.exception.AccountNotFoundException;
import api.exception.NotEnoughBalanceException;
import api.model.Account;
import api.repository.AccountRepository;
import api.util.DtoUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.*;

public class AccountServiceTest {
    private static Logger log = LoggerFactory.getLogger(AccountServiceTest.class);

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

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testMoneyTransferWithMultipleThreads() {
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000011L, 10000022L, new BigDecimal("100"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000011L, 10000022L, new BigDecimal("200"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000022L, 10000011L, new BigDecimal("300"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000022L, 10000011L, new BigDecimal("250"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000011L, 10000022L, new BigDecimal("150"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000011L, 10000022L, new BigDecimal("50"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000022L, 10000011L, new BigDecimal("450"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000022L, 10000011L, new BigDecimal("350"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000011L, 10000022L, new BigDecimal("200"))));
        executor.submit(transferMoneyCallable(new MoneyTransferDTO(10000022L, 10000011L, new BigDecimal("300"))));

        awaitTerminationAfterShutdown(executor);
        assertBalances(10000011L, 10000022L);
   }

   private void assertBalances(Long fromAccountNumber, Long toAccountNumber) {
       try {
           AccountDTO account_11 = DtoUtils.toAccountDto(accountService.getAccount(fromAccountNumber));
           AccountDTO account_22 = DtoUtils.toAccountDto(accountService.getAccount(toAccountNumber));

           log.info("Bal 11 : " + account_11.getBalanceAmount() + ", Bal 22 : " + account_22.getBalanceAmount());

           Assert.assertEquals(BigDecimal.valueOf(1950), account_11.getBalanceAmount());
           Assert.assertEquals(BigDecimal.valueOf(1050), account_22.getBalanceAmount());
       } catch (AccountNotFoundException | NotEnoughBalanceException e) {
           log.error("Error occurred while transferring money: ", e);
       }
   }

   private Callable<Account> transferMoneyCallable(MoneyTransferDTO dto) {
       return () -> {
           try {
               log.info(String.format("transferred %s from %s to %s", dto.getAmount(), dto.getFromAccountNumber(), dto.getToAccountNumber()));
               return accountService.transferMoney(dto);
           } catch (AccountNotFoundException | NotEnoughBalanceException e) {
               log.error("Error occurred while transferring money: ", e);
           } finally {
               return null;
           }
       };
   }
}
