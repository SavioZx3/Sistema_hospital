package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class LoginController {

    // Agora o Hospital é passado para o Controller
    private Hospital hospital;

    public LoginController(Hospital hospital) {
        this.hospital = hospital;
    }

    public Usuario autenticar(String email, String senha) {
        return hospital.autenticar(email, senha);
    }
}
