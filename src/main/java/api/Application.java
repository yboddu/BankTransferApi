package api;

import api.controller.MoneyTransferController;
import api.exception.AccountNotFoundException;
import api.exception.BadMoneyTransferDataException;
import api.exception.NotEnoughBalanceException;
import api.repository.AccountRepository;
import api.service.AccountService;
import api.util.ResponseUtils;
import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;

import static spark.Spark.*;

public class Application {
    public static AccountService accountService;
    public static AccountRepository accountRepository;

    public static Gson gson;

    public static void main(String[] args) {
        //initialise dependencies
        accountRepository = new AccountRepository();
        accountService = new AccountService(accountRepository);
        gson = new Gson();

        //spark config
        port(4567);
        //set response content-type as json
        before((req, res) -> res.type("application/json"));

        //routes
        Gson gson = new Gson();
        get("/api/account/:accountNumber", MoneyTransferController.getAccount);
        post("/api/money/transfer",  "application/json", MoneyTransferController.transferMoneyBetweenAccounts);

        //exception handling
        exception(AccountNotFoundException.class, (exception, request, response) -> {
            ResponseUtils.setResponseBodyAndStatusForErrors(response, HttpStatus.NOT_FOUND_404, exception.getMessage());
        });

        exception(NotEnoughBalanceException.class, (exception, request, response) -> {
            ResponseUtils.setResponseBodyAndStatusForErrors(response, HttpStatus.BAD_REQUEST_400, exception.getMessage());
        });

        exception(BadMoneyTransferDataException.class, (exception, request, response) -> {
            ResponseUtils.setResponseBodyAndStatusForErrors(response, HttpStatus.BAD_REQUEST_400, exception.getMessage());
        });

    }


}
