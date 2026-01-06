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
                    consultaController.listarAgendaMedico();
                    break;
                case 2:
                    consultaController.listarConsultas();
                    break;
                case 3:
                    emitirAtestado();
                    break;
                case 4:
                    solicitarExame();
                    break;
                case 5:
                    emitirReceita();
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

    ///MOCKS PARA OS TESTES, DEPOIS VOU TROCAR PARA CHAMADAS REAIS//
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

    private static void solicitarExame(){
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Exame: ");
        String exame= scanner.nextLine();

        System.out.print("Data: ");
        String data = scanner.nextLine();

        medicoController.solicitarExame(cpf, exame, data);
    }

    private static void emitirReceita(){
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Medicamento: ");
        String medicamento = scanner.nextLine();

        System.out.print("Posologia: ");
        String posologia = scanner.nextLine();

        medicoController.emitirReceita(cpf, medicamento, posologia);
    }
}
