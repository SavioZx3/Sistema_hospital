package ufjf.dcc025.hospital.usuarios;

public class Recepcionista extends Usuario {

    public Recepcionista(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String getTipo() {
        return "RECEPCIONISTA";
    }
}