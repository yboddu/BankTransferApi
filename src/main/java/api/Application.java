package api;

import api.controller.MoneyTransferController;
import api.dto.ErrorDTO;
import api.exception.AccountNotFoundException;
import api.exception.BadMoneyTransferDataException;
import api.exception.NotEnoughBalanceException;
import api.repository.AccountRepository;
import api.service.AccountService;
import api.util.ResponseUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.eclipse.jetty.http.HttpParser.LOG;
import static spark.Spark.*;

public class Application {
    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static AccountService accountService;
    public static AccountRepository accountRepository;

    public static Gson gson;

    public static void main(String[] args) {
        //initialise dependencies
        accountRepository = new AccountRepository();
        accountService = new AccountService(accountRepository);
        gson = new Gson();

        //spark config
        port(Integer.valueOf(args[0]));
        //set response content-type as json
        before((req, res) -> res.type("application/json"));
        after((request, response) -> {
            LOG.info(String.format("%s %s %s", new Date(), request.requestMethod(), request.url()));
        });

        //routes
        Gson gson = new Gson();
        get("/api/account/:accountNumber", MoneyTransferController.getAccount);
        put("/api/account/transfer",  "application/json", MoneyTransferController.transferMoneyBetweenAccounts);

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

        exception(JsonSyntaxException.class, (exception, request, response) -> {
            exception.printStackTrace();
            ResponseUtils.setResponseBodyAndStatusForErrors(response, HttpStatus.BAD_REQUEST_400, "Invalid type: " + exception.getCause().getMessage());
        });

        internalServerError((request, response) -> {
            return gson.toJson(
                    ErrorDTO.builder()
                    .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR_500))
                    .errorMessage("Unexpected error occurred on the server.")
                    .build());
        });
    }


}
