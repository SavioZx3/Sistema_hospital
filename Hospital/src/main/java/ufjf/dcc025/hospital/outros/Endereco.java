package ufjf.dcc025.hospital.outros;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class Endereco {
    private String logradouro;
    
    
    public Endereco(String rua , String bairro, String cidade, String numero ) throws DadosInvalidosException {
        
        atualizarEndereco(rua, bairro, cidade, numero);
    }
    
    private void atualizarEndereco(String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        
        validar(cidade);
        validar(bairro);
        validar(rua);
        validar(numero);
        
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

    public void setLogradouro(String rua , String bairro, String cidade, String numero ) throws DadosInvalidosException {
        atualizarEndereco(rua,bairro,cidade,numero);
    }

    @Override
    public String toString() {
        return logradouro;
    }
}