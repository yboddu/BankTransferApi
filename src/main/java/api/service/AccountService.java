package api.service;

import api.dto.MoneyTransferDTO;
import api.exception.AccountNotFoundException;
import api.exception.NotEnoughBalanceException;
import api.model.Account;
import api.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountService {
    private Logger log = LoggerFactory.getLogger(AccountService.class);

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(long accountNumber) {
        return accountRepository.getAccountByAccountNumber(accountNumber);
    }

    public Account transferMoney(MoneyTransferDTO moneyTransferDTO) throws AccountNotFoundException, NotEnoughBalanceException {

        Account fromAccount = accountRepository.getAccountByAccountNumber(moneyTransferDTO.getFromAccountNumber());
        Account toAccount = accountRepository.getAccountByAccountNumber(moneyTransferDTO.getToAccountNumber());

        fromAccount.withdrawFunds(moneyTransferDTO.getAmount());
        toAccount.depositFunds(moneyTransferDTO.getAmount());

        return accountRepository.getAccountByAccountNumber(moneyTransferDTO.getFromAccountNumber());
    }
}
