package ufjf.dcc025.hospital.view;

import java.util.Scanner;
import ufjf.dcc025.hospital.controller.PacienteController;

/**
 * Interface do paciente.
 * 
 * Exibe informações relacionadas ao paciente autenticado.
 */
public class PacienteInterface {

    private static Scanner scanner = new Scanner(System.in);
    private static PacienteController controller = new PacienteController();

    /**
     * Exibe o menu principal do paciente.
     */
    public static void exibir() {
        int opcao;

        do {
            System.out.println("\n=== INTERFACE PACIENTE ===");
            System.out.println("1. Ver consultas");
            System.out.println("2. Ver exames");
            System.out.println("3. Ver receitas");
            System.out.println("4. Ver atestados");
            System.out.println("0. Voltar");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> verConsultas();
                case 2 -> verExames();
                case 3 -> verReceitas();
                case 4 -> verAtestados();
                case 0 -> System.out.println("Voltando ao menu de login...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private static void verConsultas() {
        System.out.println("\n=== MINHAS CONSULTAS ===");
        controller.listarConsultas().forEach(System.out::println);
    }

    private static void verExames() {
        System.out.println("\n=== MEUS EXAMES ===");
        controller.listarExames().forEach(System.out::println);
    }

    private static void verReceitas() {
        System.out.println("\n=== MINHAS RECEITAS ===");
        controller.listarReceitas().forEach(System.out::println);
    }

    private static void verAtestados() {
        System.out.println("\n=== MEUS ATESTADOS ===");
        controller.listarAtestados().forEach(System.out::println);
    }
}