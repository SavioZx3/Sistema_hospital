package ufjf.dcc025.hospital.outros;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class Endereco {
    private String logradouro;
    
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    
    public Endereco(String rua , String bairro, String cidade, String numero ) throws DadosInvalidosException {
        
        atualizarEndereco(rua, bairro, cidade, numero);
    }
    
    private void atualizarEndereco(String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        
        validar(cidade);
        validar(bairro);
        validar(rua);
        validar(numero);
        
        // somente para a impressao na hora de editar os dados do paciente
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        
        if(!rua.toUpperCase().contains("RUA")){
            rua = "Rua " + rua;
        }
        
        this.logradouro = "Cidade: " + cidade +
                          "\nBairro " + bairro + 
                          "\n" + rua + 
                          "\nNúmero: " + numero;
    }

    private void validar(String endereco) throws DadosInvalidosException {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new DadosInvalidosException("O endereço não pode estar vazio.");
        }
    }

    public String getLogradouro() {
        return logradouro;
    }
    
    // somente para a impressao na hora de editar os dados do paciente
    public String getCidade() {
        return cidade;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }
    
    public void setLogradouro(String rua , String bairro, String cidade, String numero ) throws DadosInvalidosException {
        atualizarEndereco(rua,bairro,cidade,numero);
    }

    @Override
    public String toString() {
        return logradouro;
    }
}