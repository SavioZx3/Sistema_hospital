package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.ConsultaController;
import java.util.Scanner;

public class SecretariaInterface {

    private static Scanner scanner = new Scanner(System.in);
    private static ConsultaController consultaController = new ConsultaController();

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
                    consultaController.agendarConsulta();
                    break;
                case 2:
                    reagendarConsulta();
                    break;
                case 3:
                    consultaController.cancelarConsulta();
                    break;
                case 4:
                    cadastrarPaciente();
                    break;
                case 5:
                    consultaController.listarConsultasDoDia();
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

}
