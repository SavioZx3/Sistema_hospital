package ufjf.dcc025.hospital.outros;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class Endereco {
    private String logradouro;
    
    public Endereco(String endereco) throws DadosInvalidosException {
        validar(endereco);
        this.logradouro = endereco.trim();
    }

    private void validar(String endereco) throws DadosInvalidosException {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new DadosInvalidosException("O endereço não pode estar vazio.");
        }
        
        if (endereco.trim().length() < 5) {
            throw new DadosInvalidosException("Endereço muito curto. Informe rua e número.");
        }
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) throws DadosInvalidosException {
        validar(logradouro);
        this.logradouro = logradouro.trim();
    }

    @Override
    public String toString() {
        return logradouro;
    }
}