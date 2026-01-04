package ufjf.dcc025.hospital;

import java.util.Scanner;

public class Hospital {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        menuPrincipal();
    }

    private static void menuPrincipal(){
        int opcao;
        do{
            System.out.println("\n===SISTEMA HOSPITALAR===");
            System.out.println("1. Login");
            System.out.println("2. Sair");
            System.out.println("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1: 
                    realizarLogin();
                    break;
                case 2:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while(opcao != 2);
    }

    private static void menuPaciente(){
        int opcao;
        do {
            System.out.println("\n===MENU PACIENTE===");
            System.out.println("1. Ver consulta");
            System.out.println("2. Ver exames");
            System.out.println("3. Ver receitas");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1: 
                    System.out.println("Consultas em desenvolvimento...");
                    break;
                case 2:
                    System.out.println("Exames em desenvolvimento...");
                    break;
                case 3:
                    System.out.println("Receitas em desenvolvimento...");
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while(opcao != 0);
    }

    private static void realizarLogin(){
        int opcao;
        do{
            System.out.println("\n===LOGIN===");
            System.out.println("1. Paciente");
            System.out.println("2. Médico");
            System.out.println("3. Recepcionista");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1 -> menuPaciente();
                case 2 -> menuMedico();
                case 3 -> menuRecepcionista();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        } while(opcao != 0);
    }

    private static void menuMedico(){
        int opcao;
    do {
        System.out.println("\n=== MENU MÉDICO ===");
        System.out.println("1. Ver agenda");
        System.out.println("2. Ver consultas");
        System.out.println("3. Emitir atestado");
        System.out.println("0. Voltar");

        opcao = scanner.nextInt();
        scanner.nextLine();

        switch(opcao){
            case 1 -> System.out.println("Agenda em desenvolvimento...");
            case 2 -> System.out.println("Consultas em desenvolvimento...");
            case 3 -> System.out.println("Atestado em desenvolvimento...");
            case 0 -> System.out.println("Voltando...");
            default -> System.out.println("Opção inválida!");
        }
    } while(opcao != 0);
    }

    private static void menuRecepcionista(){
        System.out.println("\n=== MENU RECEPCIONISTA ===");
        System.out.println("Funcionalidade em desenvolvimento...");
    }
}
