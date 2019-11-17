package api.util;

import api.dto.ErrorDTO;
import spark.Response;

import static api.Application.gson;

public class ResponseUtils {
    public static void setResponseBodyAndStatusForErrors(Response response, int httpStatusCode, String message) {
        response.status(httpStatusCode);
        response.body(gson.toJson(ErrorDTO.builder()
                .code(String.valueOf(httpStatusCode))
                .errorMessage(message)
                .build()
        ));
    }
}
