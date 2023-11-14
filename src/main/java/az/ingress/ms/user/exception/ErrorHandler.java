package az.ingress.ms.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.ingress.ms.user.exception.ExceptionMessages.UNEXPECTED_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception exception) {
        logger.error("Exception: {}", exception);
        return new ErrorResponse(UNEXPECTED_ERROR.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException exception) {
        logger.error("NotFoundException: {}", exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(InternalException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(InternalException exception) {
        logger.error("InternalException: {}", exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(PasswordMismatchException exception) {
        logger.error("PasswordMismatchException: {}", exception);
        return new ErrorResponse(exception.getMessage());
    }
}
