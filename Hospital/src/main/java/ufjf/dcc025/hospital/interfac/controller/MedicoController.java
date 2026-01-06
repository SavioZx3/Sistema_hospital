package ufjf.dcc025.hospital.interfac.controller;

/**
 * Controller responsável pelas ações realizadas pelo médico.
 * 
 * Utiliza operações simuladas para testes da interface.
 */
public class MedicoController {

    /**
     * Emite um atestado médico (mock).
     */
    public void emitirAtestado(String cpf, int dias, String mensagem) {
        System.out.println("\nAtestado emitido com sucesso!");
        System.out.println("CPF: " + cpf);
        System.out.println("Dias: " + dias);
        System.out.println("Mensagem: " + mensagem);
    }

    /**
     * Solicita um exame para o paciente (mock).
     */
    public void solicitarExame(String cpf, String exame, String data) {
        System.out.println("\nExame solicitado!");
        System.out.println("CPF: " + cpf);
        System.out.println("Exame: " + exame);
        System.out.println("Data: " + data);
    }

    /**
     * Emite uma receita médica (mock).
     */
    public void emitirReceita(String cpf, String medicamento, String posologia) {
        System.out.println("\nReceita emitida!");
        System.out.println("CPF: " + cpf);
        System.out.println("Medicamento: " + medicamento);
        System.out.println("Posologia: " + posologia);
    }
}