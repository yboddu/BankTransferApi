package api.repository;

import api.data.BankAccountList;
import api.exception.AccountNotFoundException;
import api.model.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {

    private InMemoryAccountDataStore dataStore = new InMemoryAccountDataStore();

    public Account getAccountByAccountNumber(Long accountNumber) throws AccountNotFoundException {
        return dataStore.getAccount(accountNumber);
    }

    public void storeAccount(Account account) throws AccountNotFoundException {
        dataStore.storeAccount(account);
    }

    //In memory data store
    private class InMemoryAccountDataStore {
        private Map<Long, Account> accountsMap = new ConcurrentHashMap<>();

        public InMemoryAccountDataStore() {
            BankAccountList.accountList
                    .forEach(account -> accountsMap.put(account.getAccountNumber(), account));
        }

        public Account getAccount(Long accountNumber) throws AccountNotFoundException {
            checkAccountNumberExists(accountNumber);
            return accountsMap.get(accountNumber);
        }

        public void storeAccount(Account account) throws AccountNotFoundException {
            checkAccountNumberExists(account.getAccountNumber());
            accountsMap.put(account.getAccountNumber(), account);
        }

        private void checkAccountNumberExists(Long accountNumber) throws AccountNotFoundException {
            if(accountsMap.get(accountNumber) == null) {
                throw new AccountNotFoundException(String.format("Account with id %s does not exist.", accountNumber));
            }
        }
    }
}
