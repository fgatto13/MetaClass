package com.commigo.metaclass.exceptions;

import com.commigo.metaclass.utility.response.types.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(MismatchJsonProperty.class)
  public ResponseEntity<Response<Boolean>> handleMismatchedInputException(MismatchJsonProperty ex) {

    // Restituisci una risposta con uno stato 400 (Bad Request) e il messaggio personalizzato
    return ResponseEntity.badRequest()
        .body(new Response<>(false, "errore nella richiesta: " + ex.getMessage()));
  }

  @ExceptionHandler(DataFormatException.class)
  public ResponseEntity<Response<Object>> handleDataFormatException(DataFormatException ex) {
    return ResponseEntity.badRequest().body(new Response<>(null, ex.getMessage()));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Response<Object>> handleMissingParamException(
      MissingServletRequestParameterException ex) {
    String paramName = ex.getParameterName();
    String errorMessage = "Il parametro '" + paramName + "' è mancante nella richiesta.";
    return ResponseEntity.badRequest().body(new Response<>(null, errorMessage));
  }

  @ExceptionHandler(NumberFormatException.class)
  public ResponseEntity<Response<Object>> handleNumberFormatException(NumberFormatException ex) {
    return ResponseEntity.badRequest().body(new Response<>(null, "Formato parametri errato"));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Response<Object>> handleNotFoundException(NoHandlerFoundException ex) {
    String errorMessage = "URL non trovato: " + ex.getRequestURL();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(null, errorMessage));
  }
}