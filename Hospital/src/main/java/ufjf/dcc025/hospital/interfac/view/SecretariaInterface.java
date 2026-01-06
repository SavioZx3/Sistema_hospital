package ufjf.dcc025.hospital.interfac.view;

import ufjf.dcc025.hospital.interfac.controller.ConsultaController;
import java.util.Scanner;

/**
 * Interface da recepcionista.
 * ufjf.dcc025.hospital.interfac.view
 * Responsável por agendamento e gerenciamento de consultas.
 */
public class SecretariaInterface {

    private static Scanner scanner = new Scanner(System.in);
    private static ConsultaController consultaController = new ConsultaController();

    /**
     * Exibe o menu da recepcionista.
     */
    public static void exibir() {
        int opcao;

        do {
            System.out.println("\n=== INTERFACE RECEPCIONISTA ===");
            System.out.println("1. Agendar consulta");
            System.out.println("2. Reagendar consulta");
            System.out.println("3. Cancelar consulta");
            System.out.println("4. Cadastrar paciente");
            System.out.println("5. Listar consultas do dia");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> consultaController.agendarConsulta();
                case 2 -> reagendarConsulta();
                case 3 -> consultaController.cancelarConsulta();
                case 4 -> cadastrarPaciente();
                case 5 -> consultaController.listarConsultasDoDia();
                case 0 -> System.out.println("Voltando ao menu de login...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    /**
     * Reagenda uma consulta (mock).
     */
    private static void reagendarConsulta() {
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Nova data: ");
        String data = scanner.nextLine();

        System.out.print("Novo horário: ");
        String horario = scanner.nextLine();

        System.out.println("\nConsulta reagendada com sucesso!");
        System.out.println("(mock)");
        System.out.println("CPF: " + cpf);
        System.out.println("Data: " + data);
        System.out.println("Horário: " + horario);
    }

    /**
     * Cadastro de paciente (mock).
     */
    private static void cadastrarPaciente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.println("\nPaciente cadastrado com sucesso!");
        System.out.println("(mock)");
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Telefone: " + telefone);
    }
}