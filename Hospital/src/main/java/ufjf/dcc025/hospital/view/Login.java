package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.LoginController;
import java.util.Scanner;

public class Login {

    private static Scanner scanner = new Scanner(System.in);
    private static LoginController controller = new LoginController();

    public static void exibir() {
        int opcao;
        do {
            System.out.println("\n=== LOGIN ===");
            System.out.println("1. Entrar");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                System.out.print("Email: ");
                String email = scanner.nextLine();

                LoginController.TipoUsuario tipo = controller.autenticar(email);

                switch (tipo) {
                    case PACIENTE -> PacienteInterface.exibir();
                    case MEDICO -> MedicoInterface.exibir();
                    case RECEPCIONISTA -> SecretariaInterface.exibir();
                }
            }

        } while (opcao != 0);

        System.out.println("Encerrando sistema...");
    }
}
