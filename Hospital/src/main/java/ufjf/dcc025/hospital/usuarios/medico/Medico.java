package ufjf.dcc025.hospital.usuarios.medico;

import ufjf.dcc025.hospital.usuarios.Usuario;

public class Medico extends Usuario {

    public Medico(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String getTipo() {
        return "MEDICO";
    }
}