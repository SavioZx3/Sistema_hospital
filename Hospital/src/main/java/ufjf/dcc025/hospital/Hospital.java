package ufjf.dcc025.hospital;

import java.util.List;
import ufjf.dcc025.hospital.data.BancoDados;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class Hospital {

    private final List<Usuario> usuarios;

    public Hospital() {
        this.usuarios = BancoDados.usuarios;
    }

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                if (u.validarSenha(senha)) {
                    return u;
                }
                throw new IllegalArgumentException("Senha incorreta");
            }
        }
        throw new IllegalArgumentException("Usuário não encontrado");
    }
}