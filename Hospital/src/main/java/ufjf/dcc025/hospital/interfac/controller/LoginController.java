package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class LoginController {

    private Hospital hospital = new Hospital();

    public Usuario autenticar(String email, String senha) {
        return hospital.autenticar(email, senha);
    }
}