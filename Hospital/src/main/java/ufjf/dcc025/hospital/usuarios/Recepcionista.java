package ufjf.dcc025.hospital.usuarios;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;


public class Recepcionista extends Usuario{

    public Recepcionista(String nome, String cpf, String login, String senha) throws DadosInvalidosException {
        super(nome, cpf, login, senha);
    }

    @Override
    public String getTipo() {
        return "RECEPCIONISTA";
        
    }
    
}
