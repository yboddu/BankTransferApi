package api.data;

import api.model.Account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BankAccountList {
    public static final List<Account> accountList =
            Arrays.asList(
                    new Account(10000011L, new BigDecimal("1000"), "GBP"),
                    new Account(10000022L, new BigDecimal("2000"), "GBP"),
                    new Account(10000033L, new BigDecimal("3000"), "GBP"),
                    new Account(10000044L, new BigDecimal("4000"), "GBP"),
                    new Account(10000055L, new BigDecimal("5000"), "GBP")
            );
}
