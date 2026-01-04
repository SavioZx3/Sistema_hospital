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

    private static void realizarLogin(){
        System.out.println("Login em desenvolvimento");
    }
}
