package ufjf.dcc025.hospital.outros;

import org.apache.commons.validator.routines.EmailValidator;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import java.util.HashSet;
import java.util.Set;

public class Email {
    private String endereco;
    // Set estático para garantir que nenhum e-mail se repita no sistema 
    private static Set<String> emailsCadastrados = new HashSet<>();

    public Email(String endereco) throws DadosInvalidosException {
        validarFormato(endereco);
        validarUnicidade(endereco);
        
        // Armazenamos sempre em minúsculo para evitar erros
        this.endereco = endereco.toLowerCase().trim();
        emailsCadastrados.add(this.endereco);
    }

    private void validarFormato(String email) throws DadosInvalidosException {
    
        EmailValidator validator = EmailValidator.getInstance();
        
        if (email == null || !validator.isValid(email)) {
            throw new DadosInvalidosException("Formato inválido de E-mail: " + email);
        }
    }

    private void validarUnicidade(String email) throws DadosInvalidosException {
        String emailFormatado = email.toLowerCase().trim();
        if (emailsCadastrados.contains(emailFormatado)) {
            throw new DadosInvalidosException("Este e-mail já está cadastrado no sistema.");
        }
    }

    public static void limparEmailsCadastrados() {
        emailsCadastrados.clear();
    }

    // Método para quando um usuário for deletado ou o e-mail for alterado
    public static void removerEmail(String email) {
        if (email != null) {
            emailsCadastrados.remove(email.toLowerCase().trim());
        }
    }

    public String getEmail() {
        return endereco;
    }

    @Override
    public String toString() {
        return endereco;
    }
}