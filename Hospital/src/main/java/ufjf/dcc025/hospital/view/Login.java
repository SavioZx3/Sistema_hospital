package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.LoginController;
import java.util.Scanner;

public class Login {

    private static Scanner scanner = new Scanner(System.in);
    private static LoginController controller = new LoginController();

    public static void exibir() {
        int opcao;
        do {
            System.out.println("SISTEMA HOSPITAL");
            System.out.println("1. Entrar");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                realizarLogin();
            } 
            else if (opcao == 0) {
                System.out.print("Deseja realmente sair? (s/n): ");
                String confirmacao = scanner.nextLine();

                if (confirmacao.equalsIgnoreCase("s")) {
                    System.out.println("\nEncerrando sistema...");
                    break;
                } else {
                    opcao = -1; // força continuar no menu
                }
            } 
            else {
                System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);
        System.out.println("Encerrando sistema...");
    }

    /// MOCK DE LOGIN — autenticação simplificada por email
    private static void realizarLogin() {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();

        LoginController.TipoUsuario tipo = controller.autenticar(email);

        System.out.println("\nLogin realizado com sucesso!");
        System.out.println("Perfil identificado: " + tipo);
        System.out.println("---------------------------------");

        switch (tipo) {
            case PACIENTE:
                PacienteInterface.exibir();
                break;

            case MEDICO:
                MedicoInterface.exibir();
                break;

            case RECEPCIONISTA:
                SecretariaInterface.exibir();
                break;

            default:
                System.out.println("Perfil inválido.");
                break;
        }
    }
}
