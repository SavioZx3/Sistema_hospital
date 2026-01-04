package ufjf.dcc025.hospital.view;

import java.util.Scanner;

public class PacienteInterface {

    private static Scanner scanner = new Scanner(System.in);   

    public static void exibir(){
        int opcao;
        do{
            System.out.println("\n===INTERFACE PACIENTE===");
            System.out.println("1. Ver consultas");
            System.out.println("2. Ver exames");
            System.out.println("3. Ver receitas");
            System.out.println("4. Ver atestados");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    System.out.println("Funcionalidade de ver consultas em desenvolvimento...");
                    break;
                case 2:
                    System.out.println("Funcionalidade de ver exames em desenvolvimento...");
                    break;
                case 3:
                    System.out.println("Funcionalidade de ver receitas em desenvolvimento...");
                    break;
                case 4:
                    System.out.println("Funcionalidade de ver atestados em desenvolvimento...");
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
