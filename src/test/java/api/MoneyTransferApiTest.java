package api;

import api.dto.AccountDTO;
import api.dto.MoneyTransferDTO;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.*;
import spark.Spark;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransferApiTest {
    private Gson gson = new Gson();

    @BeforeClass
    public static void setup() {
        Application.main(new String[]{"4568"});
    }

    public void testStringAccountNumberReturns_Parsing400Error() throws UnirestException {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("fromAccountNumber", "10000011A");
        requestBodyMap.put("toAccountNumber", 10000022L);
        requestBodyMap.put("amount", new BigDecimal("50.00"));

        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:4568/api/account/transfer")
                .header("accept", "application/json")
                .body(gson.toJson(requestBodyMap))
                .asJson();

        Assert.assertEquals(400, jsonResponse.getStatus());
        Assert.assertEquals("Invalid type: For input string: \"10000011A\"", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testInvalidAccountNumber_Returns404() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                                                    MoneyTransferDTO.builder()
                                                            .fromAccountNumber(100000119999L)
                                                            .toAccountNumber(10000022L)
                                                            .amount(new BigDecimal("10.00"))
                                                            .build());

        Assert.assertEquals(404, jsonResponse.getStatus());
        Assert.assertEquals("Account with id 100000119999 does not exist.", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testMissingFromAccountNumberInPayload_Returns400Error() throws UnirestException{
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                                                    MoneyTransferDTO.builder()
                                                            .toAccountNumber(10000022L)
                                                            .amount(new BigDecimal("10.00"))
                                                            .build());

        Assert.assertEquals(400, jsonResponse.getStatus());
        Assert.assertEquals("From Account number is mandatory, provide a valid account number.", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testFromAccountNumberSameAsToAccountNumber_Returns400Error() throws UnirestException{
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                                                    MoneyTransferDTO.builder()
                                                        .fromAccountNumber(10000022L)
                                                        .toAccountNumber(10000022L)
                                                        .amount(new BigDecimal("10.00"))
                                                        .build());

        Assert.assertEquals(400, jsonResponse.getStatus());
        Assert.assertEquals("From and To Account numbers must be different.", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testTransferringMoreMoneyThanAccountBalanceReturnsInsufficientFunds_Returns400Error() throws UnirestException{
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                MoneyTransferDTO.builder()
                        .fromAccountNumber(10000011L)
                        .toAccountNumber(10000022L)
                        .amount(new BigDecimal("1100.00"))
                        .build());

        Assert.assertEquals(400, jsonResponse.getStatus());
        Assert.assertEquals("Account with id: 10000011 does not have enough funds to withdraw : 1100.00", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testTransferringMoneyLessThanZeroReturns_Returns400Error() throws UnirestException{
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                MoneyTransferDTO.builder()
                        .fromAccountNumber(10000011L)
                        .toAccountNumber(10000022L)
                        .amount(new BigDecimal("-50.00"))
                        .build());

        Assert.assertEquals(400, jsonResponse.getStatus());
        Assert.assertEquals("Amount to be transferred must be greater than 0.", jsonResponse.getBody().getObject().get("errorMessage"));
    }

    @Test
    public void testMoneyTransferIsSuccessful() throws UnirestException{
        HttpResponse<JsonNode> jsonResponse = makeMoneyTransferApiCall(
                MoneyTransferDTO.builder()
                        .fromAccountNumber(10000011L)
                        .toAccountNumber(10000022L)
                        .amount(new BigDecimal("100.00"))
                        .build());

        AccountDTO fromAccountDto = gson.fromJson(Unirest.get("http://localhost:4568/api/account/10000011").asJson().getBody().toString(), AccountDTO.class);
        AccountDTO toAccountDto = gson.fromJson(Unirest.get("http://localhost:4568/api/account/10000022").asJson().getBody().toString(), AccountDTO.class);

        Assert.assertEquals(200, jsonResponse.getStatus());
        Assert.assertEquals(new BigDecimal("900"), fromAccountDto.getBalanceAmount());
        Assert.assertEquals(new BigDecimal("2100"), toAccountDto.getBalanceAmount());
    }

    private HttpResponse<JsonNode> makeMoneyTransferApiCall(MoneyTransferDTO dto) throws UnirestException {
        return Unirest.put("http://localhost:4568/api/account/transfer")
                .header("accept", "application/json")
                .body(gson.toJson(dto))
                .asJson();
    }

    @AfterClass
    public static void teardown() {
        Spark.stop();
    }
}
