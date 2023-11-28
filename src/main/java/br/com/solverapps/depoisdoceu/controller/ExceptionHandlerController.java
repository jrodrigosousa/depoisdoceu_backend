package br.com.solverapps.depoisdoceu.controller;

import br.com.solverapps.depoisdoceu.business.AuthService;
import br.com.solverapps.depoisdoceu.business.exception.EntityNotFoundException;
import br.com.solverapps.depoisdoceu.business.exception.IncorrectEndpointUsageException;
import br.com.solverapps.depoisdoceu.data.dto.ExceptionResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
public class ExceptionHandlerController {

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDTO> authenticationError(BadCredentialsException exception){
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> authenticationError(AccessDeniedException exception){
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ExceptionResponseDTO> invalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception){
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> entityNotFoundExceptionHandler(EntityNotFoundException exception){
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IncorrectEndpointUsageException.class)
    public ResponseEntity<ExceptionResponseDTO> incorrectEndpointUsageException(IncorrectEndpointUsageException exception){
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return  ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> defaultHandler(Exception exception){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        logger.debug(stringWriter.toString());
        System.out.println(stringWriter.toString());
        ExceptionResponseDTO response = new ExceptionResponseDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.internalServerError().body(response);
    }
}
