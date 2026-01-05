package ufjf.dcc025.hospital.controller;

public class MedicoController {
    
    public void emitirAtestado(String cpf, int dias, String mensagem){
        System.out.println("Atestado emitido para CPF: " + cpf);
    }

    public void solicitarExame(String cpf, String tipoExame, String data){
        System.out.println("Exame solicitado para CPF: " + cpf);
    }

    public void emitirReceita(String cpf, String medicamento, String quantidade){
        System.out.println("Receita emitada para CPF: " + cpf);
    }
}
