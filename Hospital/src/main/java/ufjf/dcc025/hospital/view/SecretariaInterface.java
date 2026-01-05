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
                    agendarConsulta();
                    break;
                case 2:
                    reagendarConsulta();
                    break;
                case 3:
                    cancelarConsulta();
                    break;
                case 4:
                    cadastrarPaciente();
                    break;
                case 5:
                    listarConsultasDoDia();
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
    
    private static void agendarConsulta(){
        System.out.println("\n=== AGENDAR CONSULTA ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Data da consulta (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        System.out.print("Horário da consulta: ");
        String horario = scanner.nextLine();

        System.out.println("\nConsulta agendada com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
        System.out.println("Data: " + data + " | Horário: " + horario);
    }

    private static void reagendarConsulta(){
        System.out.println("\n=== REAGENDAR CONSULTA ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Nova data (dd/mm/aaaa): ");
        String novaData = scanner.nextLine();

        System.out.print("Novo horário: ");
        String novoHorario = scanner.nextLine();

        System.out.println("\nConsulta reagendada com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
        System.out.println("Nova data: " + novaData + " | Novo horário: " + novoHorario);
    }

    private static void cancelarConsulta(){
        System.out.println("\n=== CANCELAR CONSULTA ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.println("\nConsulta cancelada com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
    }

    private static void cadastrarPaciente(){
        System.out.println("\n=== CADASTRO DE PACIENTE ===");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.println("\nPaciente cadastrado com sucesso!");
        System.out.println("(mock) Nome: " + nome + " | CPF: " + cpf + " | Telefone: " + telefone);
    }

    private static void listarConsultasDoDia(){
        System.out.println("\n=== CONSULTAS DO DIA ===");
        System.out.println("09:00 - Paciente: João | Médico: Dr. Carlos");
        System.out.println("10:30 - Paciente: Maria | Médico: Dra. Ana");
        System.out.println("14:00 - Paciente: Pedro | Médico: Dr. João");
        System.out.println("(dados simulados)");
    }

}
