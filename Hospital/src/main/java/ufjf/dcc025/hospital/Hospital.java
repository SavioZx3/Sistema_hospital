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
        System.out.println("Login: ");
        String login = scanner.nextLine();

        System.out.println("Senha:");
        String senha = scanner.nextLine();

        System.out.println("Login realizado(mock)");

        //por agora vou jogar direto pro menu paciente
        menuPaciente();
    }
}
