/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
package ufjf.dcc025.hospital.outros;

import java.util.HashSet;
import java.util.Set;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;


public class Cpf {
 
    // Lista estática que guarda todos os números de CPF já criados no sistema
    private static final Set<String> cpfsCadastrados = new HashSet<>();
    
    private final String numero;
    
    
    public Cpf(String cpf)throws DadosInvalidosException{
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidosException("CPF não pode ser nulo");
        }
        
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if(cpfLimpo.length() != 11){
            throw new DadosInvalidosException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
        
        if(cpfsCadastrados.contains(cpfLimpo)){
            throw new DadosInvalidosException("Este CPF já está cadastrado no sistema.");
        }
        
        // se passou em tudo salva e adiciona a lista
        numero = cpfLimpo;  
        cpfsCadastrados.add(cpfLimpo);
    }
    
    public String getNumero(){ return numero; }
    
    public static void limparCpfCadastrados() {
        cpfsCadastrados.clear();
    }
    
    @Override
    public String toString() {
        return numero;
    }
}
