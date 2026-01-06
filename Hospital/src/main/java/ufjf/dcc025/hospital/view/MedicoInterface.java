package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.ConsultaController;
import ufjf.dcc025.hospital.controller.MedicoController;
import java.util.Scanner;

public class MedicoInterface {

    private static Scanner scanner = new Scanner(System.in);
    private static ConsultaController consultaController = new ConsultaController();
    private static MedicoController medicoController = new MedicoController();

    public static void exibir(){
        int opcao;

        do{
            System.out.println("\n=================================");
            System.out.println("          INTERFACE MÉDICO");
            System.out.println("=================================");
            System.out.println("1. Ver agenda");
            System.out.println("2. Ver consultas");
            System.out.println("3. Emitir atestado");
            System.out.println("4. Solicitar exame");
            System.out.println("5. Emitir receita");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1 -> consultaController.listarAgendaMedico();
                case 2 -> consultaController.listarConsultas();
                case 3 -> emitirAtestado();
                case 4 -> solicitarExame();
                case 5 -> emitirReceita();
                case 0 -> System.out.println("Voltando ao menu de login...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    /// ================= MOCKS =================

    private static void emitirAtestado() {
        System.out.println("\n=== EMISSÃO DE ATESTADO ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Duração (em dias): ");
        int dias = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Mensagem do atestado: ");
        String mensagem = scanner.nextLine();

        medicoController.emitirAtestado(cpf, dias, mensagem);
    }

    private static void solicitarExame(){
        System.out.println("\n=== SOLICITAÇÃO DE EXAME ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Tipo de exame: ");
        String exame = scanner.nextLine();

        System.out.print("Data do exame (dd/mm/aaaa): ");
        String data = scanner.nextLine();

        medicoController.solicitarExame(cpf, exame, data);
    }

    private static void emitirReceita(){
        System.out.println("\n=== EMISSÃO DE RECEITA ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Medicamento: ");
        String medicamento = scanner.nextLine();

        System.out.print("Posologia: ");
        String posologia = scanner.nextLine();

        medicoController.emitirReceita(cpf, medicamento, posologia);
    }
}