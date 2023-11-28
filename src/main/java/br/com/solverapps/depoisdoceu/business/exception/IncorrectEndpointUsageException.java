package br.com.solverapps.depoisdoceu.business.exception;

public class IncorrectEndpointUsageException extends RuntimeException{
    public IncorrectEndpointUsageException(String message){
        super(message);
    }
}
