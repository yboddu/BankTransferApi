package api.controller;

import api.dto.MoneyTransferDTO;
import api.exception.BadMoneyTransferDataException;
import api.model.Account;
import api.util.DtoUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;

import static api.Application.accountService;
import static api.Application.gson;

public class MoneyTransferController {
    public static Route transferMoneyBetweenAccounts = (Request request, Response response) -> {
        MoneyTransferDTO moneyTransferDTO = gson.fromJson(request.body(), MoneyTransferDTO.class);
        validateMoneyTransferData(moneyTransferDTO);

        Account fromAccount = accountService.transferMoney(moneyTransferDTO);

        return gson.toJson(DtoUtils.toAccountDto(fromAccount));
    };

    public static Route getAccount = (Request request, Response response) -> {
        Account account = accountService.getAccount(Long.valueOf(request.params(":accountNumber")));
        return gson.toJson(DtoUtils.toAccountDto(account));
    };

    private static void validateMoneyTransferData(MoneyTransferDTO moneyTransferDTO) {
        if(moneyTransferDTO.getFromAccountNumber() == null) {
            throw new BadMoneyTransferDataException("From Account number is mandatory, provide a valid account number.");
        }

        if(moneyTransferDTO.getToAccountNumber() == null) {
            throw new BadMoneyTransferDataException("To Account number is mandatory, provide a valid account number.");
        }

        if(moneyTransferDTO.getFromAccountNumber().equals(moneyTransferDTO.getToAccountNumber())) {
            throw new BadMoneyTransferDataException("From and To Account numbers must be different.");
        }

        if(moneyTransferDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadMoneyTransferDataException("Amount to be transferred must be greater than 0.");
        }
    }
}
