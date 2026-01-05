package ufjf.dcc025.hospital.controller;

public class MedicoController {

    public void emitirAtestado(String cpf, int dias, String mensagem) {
        System.out.println("\nAtestado emitido com sucesso!");
        System.out.println("CPF: " + cpf);
        System.out.println("Dias: " + dias);
        System.out.println("Mensagem: " + mensagem);
    }

    public void solicitarExame(String cpf, String exame, String data) {
        System.out.println("\nExame solicitado!");
        System.out.println("CPF: " + cpf);
        System.out.println("Exame: " + exame);
        System.out.println("Data: " + data);
    }

    public void emitirReceita(String cpf, String medicamento, String posologia) {
        System.out.println("\nReceita emitida!");
        System.out.println("CPF: " + cpf);
        System.out.println("Medicamento: " + medicamento);
        System.out.println("Posologia: " + posologia);
    }
}
