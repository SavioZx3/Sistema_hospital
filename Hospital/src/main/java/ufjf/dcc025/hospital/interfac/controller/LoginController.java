package ufjf.dcc025.hospital.interfac.controller;
/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class LoginController {
    private final Hospital hospital;

    public LoginController(Hospital hospital) {
        this.hospital = hospital;
    }

    public Usuario autenticar(String email, String senha) {
        return hospital.autenticar(email, senha);
    }
}