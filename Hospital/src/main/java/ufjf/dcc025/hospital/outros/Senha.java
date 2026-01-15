package ufjf.dcc025.hospital.outros;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class Senha {
    
    private final String valor;
    
    public Senha(String senha) throws DadosInvalidosException{
        
        validar(senha);
        valor = senha;
    }
    
    private void validar(String senha) throws DadosInvalidosException {
        if (senha == null || senha.trim().isEmpty()) {
            throw new DadosInvalidosException("A senha não pode ser vazia.");
        }
        
        // Exige pelo menos 4 caracteres
        if (senha.trim().length() < 4) {
            throw new DadosInvalidosException("A senha deve ter pelo menos 4 caracteres.");
        }
    }
    
    public String getInfo() {
        return valor;
    }
    
    public boolean verificar(String tentativa) {
        return this.valor.equals(tentativa);
    }
    
    @Override
    public String toString() {
        return "****";
    }
}
