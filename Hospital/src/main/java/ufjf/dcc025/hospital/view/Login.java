package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.LoginController;
import java.util.Scanner;

public class Login {

    private static Scanner scanner = new Scanner(System.in);
    private static LoginController controller = new LoginController();

    public static void exibir(){
        int opcao;
        do{
            System.out.println("\n===LOGIN===");
            System.out.println("1. Paciente");
            System.out.println("2. Médico");
            System.out.println("3. Recepcionista");
            System.out.println("0. Voltar");
            System.out.println("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1, 2, 3 -> {
                    LoginController.TipoUsuario tipo = controller.escolheTipoUsuario(opcao);

                    if(tipo == LoginController.TipoUsuario.PACIENTE){
                        PacienteInterface.exibir();
                    }

                    else if(tipo == LoginController.TipoUsuario.MEDICO){
                        MedicoInterface.exibir();
                    }

                    else if(tipo == LoginController.TipoUsuario.RECEPCIONISTA){
                        SecretariaInterface.exibir();
                    }
                }
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        }
        while(opcao != 0);
    }
}
