package ufjf.dcc025.hospital.usuarios.paciente;

import ufjf.dcc025.hospital.usuarios.Usuario;

public class Paciente extends Usuario {

    public Paciente(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String getTipo() {
        return "PACIENTE";
    }
}