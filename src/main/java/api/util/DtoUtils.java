package api.util;

import api.dto.AccountDTO;
import api.model.Account;

public class DtoUtils {
    public static AccountDTO toAccountDto(Account accountModel) {
        return new AccountDTO(
            accountModel.getAccountNumber(),
            accountModel.getBalanceAmount(),
            accountModel.getCurrencyCode()
        );
    }
}
