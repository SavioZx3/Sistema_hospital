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
                    verAgenda();
                    break;
                case 2:
                    verConsultas();
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

    ///MOCKS PARA OS TESTES, DEPOIS VOU TROCAR PARA CHAMADAS REAIS///
    private static void verAgenda(){
        System.out.println("\n===AGENDA DO MÉDICO===");
        System.out.println("09:00 - Consulta com paciente A");
        System.out.println("10:30 - Consulta com paciente B");
        System.out.println("14:00 - Consulta com paciente C");
        System.out.println("(dados simulados para os testes)");
    }

    private static void verConsultas(){
        System.out.println("\n===CONSULTAS DO DIA===");
        System.out.println("Paciente: João | Horário: 09:00");
        System.out.println("Paciente: Maria | Horário: 10:30");
        System.out.println("(dados simulados para os testes)");
    }

    private static void emitirAtestado(){
        System.out.println("\n===Emissão de Atestado===");

        System.out.println("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.println("Duração do atestado (dias): ");
        int dias = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Mensagem do atestado:");
        String mensagem = scanner.nextLine();

        System.out.println("\nAtestado emitido com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
        System.out.println("Duração: " + dias + " dias");
        System.out.println("Mensagem: " + mensagem);
    }

    private static void solicitarExame(){
        System.out.println("\n===Solicitação de Exame===");

        System.out.println("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.println("Tipo de exame: ");
        String tipoExame = scanner.nextLine();

        System.out.println("Data do exame(dd/mm/aaaa");
        String data = scanner.nextLine();

        System.out.println("\nExame solicitado com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
        System.out.println("Exame: " + tipoExame + " | Data: " + data);
    }

    private static void emitirReceita(){
        System.out.println("\n=== EMISSÃO DE RECEITA ===");

        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();

        System.out.print("Medicamento: ");
        String medicamento = scanner.nextLine();

        System.out.print("Quantidade / Posologia: ");
        String quantidade = scanner.nextLine();

        System.out.println("\nReceita emitida com sucesso!");
        System.out.println("(mock) Paciente CPF: " + cpf);
        System.out.println("Medicamento: " + medicamento);
        System.out.println("Quantidade: " + quantidade);
    }
}
