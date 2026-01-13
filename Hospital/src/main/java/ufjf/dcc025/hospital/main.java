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
