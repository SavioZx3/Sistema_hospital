package ufjf.dcc025.hospital.view;

import java.util.Scanner;

public class Login {
    private static Scanner scanner = new Scanner(System.in);

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
                case 1:
                    PacienteInterface.exibir();
                    break;
                case 2:
                    MedicoInterface.exibir();
                    break;
                case 3:
                    SecretariaInterface.exibir();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
        while(opcao != 0);
    }
}
