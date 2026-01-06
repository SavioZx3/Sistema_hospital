package ufjf.dcc025.hospital.view;

import ufjf.dcc025.hospital.controller.ConsultaController;
import ufjf.dcc025.hospital.controller.MedicoController;
import java.util.Scanner;

/**
 * Interface do médico.
 * 
 * Responsável por exibir menus e capturar dados.
 * A lógica de negócio é delegada aos controllers.
 */
public class MedicoInterface {

    private static Scanner scanner = new Scanner(System.in);

    // Controllers utilizados pela interface
    private static ConsultaController consultaController = new ConsultaController();
    private static MedicoController medicoController = new MedicoController();

    /**
     * Exibe o menu principal do médico.
     */
    public static void exibir() {
        int opcao;

        do {
            System.out.println("\n=== INTERFACE MÉDICO ===");
            System.out.println("1. Ver agenda");
            System.out.println("2. Ver consultas");
            System.out.println("3. Emitir atestado");
            System.out.println("4. Solicitar exames");
            System.out.println("5. Emitir receita");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
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

    /**
     * Captura os dados e solicita ao controller a emissão de atestado.
     * Método mock.
     */
    private static void emitirAtestado() {
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Duração (dias): ");
        int dias = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Mensagem: ");
        String mensagem = scanner.nextLine();

        medicoController.emitirAtestado(cpf, dias, mensagem);
    }

    /**
     * Solicita um exame para um paciente (mock).
     */
    private static void solicitarExame() {
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Exame: ");
        String exame = scanner.nextLine();

        System.out.print("Data: ");
        String data = scanner.nextLine();

        medicoController.solicitarExame(cpf, exame, data);
    }

    /**
     * Emite uma receita médica (mock).
     */
    private static void emitirReceita() {
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Medicamento: ");
        String medicamento = scanner.nextLine();

        System.out.print("Posologia: ");
        String posologia = scanner.nextLine();

        medicoController.emitirReceita(cpf, medicamento, posologia);
    }
}