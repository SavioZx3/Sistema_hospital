package ufjf.dcc025.hospital.view;

import java.util.Scanner;

public class MedicoInterface {

    private static Scanner scanner = new Scanner(System.in);

    public static void exibir(){
        int opcao;
        do{
            System.out.println("\n===INTERFACE MÉDICO===");
            System.out.println("1. Ver agenda");
            System.out.println("2. Ver consultas");
            System.out.println("3. Emitir atestado");
            System.out.println("4. Solicitar exames");
            System.out.println("5. Emitir receita");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    System.out.println("Funcionalidade de ver agenda em desenvolvimento...");
                    break;
                case 2:
                    System.out.println("Funcionalidade de ver consultas em desenvolvimento...");
                    break;
                case 3:
                    System.out.println("Funcionalidade de emitir atestado em desenvolvimento...");
                    break;
                case 4:
                    System.out.println("Funcionalidade de solicitar exames em desenvolvimento...");
                    break;
                case 5:
                    System.out.println("Funcionalidade de emitir receita em desenvolvimento...");
                    break;
                case 0:
                    System.out.println("Voltando ao menu de login...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } 
        while (opcao != 0);
    }
}
