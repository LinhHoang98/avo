package com.linhhn.zync.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody Mono<HttpErrorInfo> handleNotFoundException(ServerHttpRequest request, ResourceNotFoundException ex) {
        return createHttpErrorInfo(request, ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public @ResponseBody Mono<HttpErrorInfo> handleAlreadyExistsException(ServerHttpRequest request, ResourceAlreadyExistsException ex) {
        return createHttpErrorInfo(request, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody Mono<HttpErrorInfo> handleBadRequestException(ServerHttpRequest request, BadRequestException ex) {
        return createHttpErrorInfo(request, ex);
    }

    private Mono<HttpErrorInfo> createHttpErrorInfo(ServerHttpRequest request, Exception ex) {
        return Mono.defer(() -> {
            final String path = request.getPath().pathWithinApplication().value();
            final String message = ex.getMessage();
            return Mono.just(new HttpErrorInfo(path, message));
        });
    }
}
