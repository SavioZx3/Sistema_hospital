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
                    verConsultas();
                    break;
                case 2:
                    verExames();
                    break;
                case 3:
                    verReceitas();
                    break;
                case 4:
                    verAtestados();
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

    ///MOCKS PARA OS TESTES, DEPOIS VOU TROCAR PARA CHAMADAS REAIS///
    private static void verConsultas(){
        System.out.println("\n=== MINHAS CONSULTAS ===");
        System.out.println("Data: 10/01/2026 | Horário: 09:00 | Médico: Dr. João");
        System.out.println("Data: 15/01/2026 | Horário: 14:30 | Médico: Dra. Maria");
        System.out.println("(dados simulados)");
    }

    private static void verExames(){
        System.out.println("\n=== MEUS EXAMES ===");
        System.out.println("Exame: Hemograma | Data: 12/01/2026 | Status: Realizado");
        System.out.println("Exame: Raio-X | Data: 20/01/2026 | Status: Agendado");
        System.out.println("(dados simulados)");
    }

    private static void verReceitas(){
        System.out.println("\n=== MINHAS RECEITAS ===");
        System.out.println("Medicamento: Dipirona | Quantidade: 1 comprimido a cada 8h");
        System.out.println("Medicamento: Amoxicilina | Quantidade: 500mg por 7 dias");
        System.out.println("(dados simulados)");
    }

    private static void verAtestados(){
        System.out.println("\n=== MEUS ATESTADOS ===");
        System.out.println("Data de emissão: 08/01/2026 | Duração: 3 dias");
        System.out.println("Motivo: Repouso médico");
        System.out.println("(dados simulados)");
    }

}
