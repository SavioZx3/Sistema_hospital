package ufjf.dcc025.hospital.outros;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import java.util.ArrayList;
import org.apache.commons.validator.routines.EmailValidator;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import java.util.HashSet;
import java.util.List;
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

    public void validarFormato(String email) throws DadosInvalidosException {

        EmailValidator validator = EmailValidator.getInstance();

        if (email == null || !validator.isValid(email)) {
            throw new DadosInvalidosException("Formato inválido de E-mail: " + email);
        }
    }

    public void validarUnicidade(String email) throws DadosInvalidosException {
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

    public static void adicionarEmailStatic(String email) {
        if (email != null) {
            emailsCadastrados.add(email.toLowerCase().trim());
        }
    }

    public static List<String> getTodosEmails() {
        return new ArrayList<>(emailsCadastrados);
    }

    @Override
    public String toString() {
        return endereco;
    }
}
