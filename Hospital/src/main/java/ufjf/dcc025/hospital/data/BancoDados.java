package ufjf.dcc025.hospital.data;

import java.util.ArrayList;
import java.util.List;

import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;

public class BancoDados {

    public static final List<Usuario> usuarios = new ArrayList<>();

    static {
        // ===== MÉDICOS =====
        usuarios.add(new Medico(
            "Dr. João",
            "111.111.111-11",
            "joao@hospital.com",
            "123",
            "Cardiologista"
        ));

        usuarios.add(new Medico(
            "Dr. Ana",
            "222.222.222-22",
            "ana@hospital.com",
            "123",
            "Oftamologista"
        ));

        usuarios.add(new Medico(
            "Dr. Carlos",
            "333.333.333-33",
            "carlos@hospital.com",
            "123",
            "Ortopedista"
        ));

        // ===== PACIENTES =====
        usuarios.add(new Paciente(
            "Pedro",
            "444.444.444-44",
            "pedro@email.com",
            "123",
            new Historico(),
            "Rua A, 123",
            "(32) 99999-1111"
        ));

        usuarios.add(new Paciente(
            "Maria",
            "555.555.555-55",
            "maria@email.com",
            "123",
            new Historico(),
            "Rua B, 456",
            "(32) 99999-2222"
        ));

        usuarios.add(new Paciente(
            "Lucas",
            "666.666.666-66",
            "lucas@email.com",
            "123",
            new Historico(),
            "Rua C, 789",
            "(32) 99999-3333"
        ));

        // ===== RECEPCIONISTA =====
        usuarios.add(new Recepcionista(
            "Secretaria",
            "recep@hospital.com",
            "123"
        ));
    }
}