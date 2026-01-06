package ufjf.dcc025.hospital.exception;

public class DataInvalidaException extends RuntimeException{
    
    public  DataInvalidaException (String mensagem){
        super(mensagem);
    }
}
