package home.balda.cryptosign;

import com.google.gson.Gson;
import home.balda.cryptosign.representation.CryptoSignErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Error handler that advice controller class and converts exceptions to response object
 */
@ControllerAdvice
public class CryptoSignErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NullPointerException.class,IllegalArgumentException.class})
    protected ResponseEntity<Object> handleNullPointerError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = new Gson().toJson(
                new CryptoSignErrorResponse(System.currentTimeMillis()+"",
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage()),CryptoSignErrorResponse.class);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { RuntimeException.class})
    protected ResponseEntity<Object> handleServerError(RuntimeException ex, WebRequest request) {

        String bodyOfResponse = new Gson().toJson(
                new CryptoSignErrorResponse(System.currentTimeMillis()+"",
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        ex.getMessage()),CryptoSignErrorResponse.class);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
