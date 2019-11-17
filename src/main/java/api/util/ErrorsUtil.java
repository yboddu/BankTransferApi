package api.util;

import api.dto.ErrorDTO;
import org.eclipse.jetty.http.HttpStatus;

public class ErrorsUtil {

    public static ErrorDTO to_404Error(String errorMessage) {
        return new ErrorDTO(String.valueOf(HttpStatus.NOT_FOUND_404), errorMessage);
    }

    public static ErrorDTO to_400Error(String errorMessage) {
        return new ErrorDTO(String.valueOf(HttpStatus.BAD_REQUEST_400), errorMessage);
    }
}
