package ufjf.dcc025.hospital.data;

import java.util.ArrayList;
import java.util.List;

import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class BancoDados {

    public static final List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Medico("Dr. João", "joao@hospital.com", "123"));
        usuarios.add(new Medico("Dr. Ana", "ana@hospital.com", "123"));
        usuarios.add(new Medico("Dr. Carlos", "carlos@hospital.com", "123"));

        usuarios.add(new Paciente("Pedro", "pedro@email.com", "123"));
        usuarios.add(new Paciente("Maria", "maria@email.com", "123"));
        usuarios.add(new Paciente("Lucas", "lucas@email.com", "123"));

        usuarios.add(new Recepcionista("Secretaria", "recep@hospital.com", "123"));
    }
}