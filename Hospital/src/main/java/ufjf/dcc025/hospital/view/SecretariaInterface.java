package ufjf.dcc025.hospital.view;

import java.util.Scanner;

public class SecretariaInterface {

    private static Scanner scanner = new Scanner(System.in);

    public static void exibir(){
        int opcao;
        do{
            System.out.println("\n===INTERFACE RECEPCIONISTA===");
            System.out.println("1. Agendar consulta");
            System.out.println("2. Reagendar consulta");
            System.out.println("3. Cancelar consulta");
            System.out.println("4. Cadastrar paciente");
            System.out.println("5. Listar consultas do dia");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    System.out.println("Funcionalidade de agendar consulta em desenvolvimento...");
                    break;
                case 2:
                    System.out.println("Funcionalidade de reagendar consulta em desenvolvimento...");
                    break;
                case 3:
                    System.out.println("Funcionalidade de cancelar consulta em desenvolvimento...");
                    break;
                case 4:
                    System.out.println("Funcionalidade de cadastrar paciente em desenvolvimento...");
                    break;
                case 5:
                    System.out.println("Funcionalidade de listar consultas do dia em desenvolvimento...");
                    break;
                case 0: 
                    System.out.println("Voltando ao menu de login...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
        while(opcao != 0);
    }
}
