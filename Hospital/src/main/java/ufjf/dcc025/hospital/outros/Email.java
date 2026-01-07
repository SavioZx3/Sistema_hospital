package ufjf.dcc025.hospital.outros;

import java.util.HashSet;
import java.util.Set;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class Email {
    
    private static final Set<String> emailsCadastrados = new HashSet<>();
    private final String endereco;
    
    
    public Email(String email) throws DadosInvalidosException{
     
        if(email == null || email.trim().isEmpty()){
            throw new DadosInvalidosException("E-mail não pode ser vazio.");
        }
        
        String emailTratado = email.trim().toLowerCase();
        
        if(!emailTratado.contains("@") || !emailTratado.contains(".")){
            throw new DadosInvalidosException("Formato inválido de E-mail");
        }
        
        if(emailsCadastrados.contains(emailTratado)){
            throw new DadosInvalidosException("Este e-mail já está em uso por outro usuario");
        }
        
        endereco = emailTratado;
        emailsCadastrados.add(emailTratado);
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public static void limparEmailsCadastrados() {
        emailsCadastrados.clear();
    }
    
    public static void removerEmail(String emailAntigo) {
        if (emailAntigo != null) {
            emailsCadastrados.remove(emailAntigo.trim().toLowerCase());
        }
    }
    
    @Override
    public String toString() {
        return endereco;
    }
}
