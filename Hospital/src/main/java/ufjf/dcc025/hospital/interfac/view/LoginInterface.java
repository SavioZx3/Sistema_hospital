package ufjf.dcc025.hospital.interfac.view;


import ufjf.dcc025.hospital.interfac.controller.LoginController;
import java.util.Scanner;

/**
 * Classe responsável pela interface de login do sistema.
 * ufjf.dcc025.hospital.interfac.view
 * Esta classe apenas captura os dados do usuário e
 * direciona para a interface correta conforme o tipo de usuário.
 * Não contém regras de negócio.
 */
public class LoginInterface {

    // Scanner único para entrada de dados no console
    private static Scanner scanner = new Scanner(System.in);

    // Controller responsável pela autenticação
    private static LoginController controller = new LoginController();

    /**
     * Exibe o menu inicial de login do sistema.
     */
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

                // Autenticação mock baseada no email
                LoginController.TipoUsuario tipo = controller.autenticar(email);

                // Direciona para a interface correta
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