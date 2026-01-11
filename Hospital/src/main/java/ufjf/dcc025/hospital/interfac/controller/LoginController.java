package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.data.BancoDados;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class LoginController {

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : BancoDados.usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)
                && u.validarSenha(senha)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Email ou senha inválidos");
    }
}