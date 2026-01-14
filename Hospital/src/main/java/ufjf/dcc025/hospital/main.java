/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/

package ufjf.dcc025.hospital;

import ufjf.dcc025.hospital.interfac.view.LoginInterface;

public class main {
    public static void main(String[] args) {
        // Inicializa o Hospital (que por sua vez carrega o BancoDados)
        Hospital hospital = new Hospital();
        
        // Inicia pela tela de Login
        new LoginInterface(hospital);
    }
}
