package ufjf.dcc025.hospital.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;

public class BancoDados {
    public static final List<Usuario> usuarios = new ArrayList<>();

    static {
        try {
            // 1. Médicos
            Medico joao = new Medico("Dr. João Silva", "11111111111", "joao@hospital.com", "1234", "Cardiologia");
            Medico ana = new Medico("Dra. Ana Costa", "22222222222", "ana@hospital.com", "1234", "Pediatria");

            // 2. Gerar Consultas para AMBOS os médicos (Ajuste solicitado)
            LocalDate amanha = LocalDate.now().plusDays(1);
            
            // Horários para o Dr. João (Manhã)
            for (int i = 8; i <= 11; i++) {
                Consulta c = new Consulta(amanha, LocalTime.of(i, 0), LocalTime.of(i, 30), joao);
                joao.getAgenda().adicionarConsultaManual(c);
            }
            
            // Horários para a Dra. Ana (Tarde)
            for (int i = 14; i <= 17; i++) {
                Consulta c = new Consulta(amanha, LocalTime.of(i, 0), LocalTime.of(i, 30), ana);
                ana.getAgenda().adicionarConsultaManual(c);
            }

            usuarios.add(joao);
            usuarios.add(ana);

            // 3. Paciente
            usuarios.add(new Paciente("Pedro Rocha", "44444444444", "pedro@email.com", "1234", 
                                     "32988881111", "Juiz de Fora", "Centro", "Rua Principal", "10"));

            // 4. Recepcionista
            usuarios.add(new Recepcionista("Secretaria", "77777777777", "recep@hospital.com", "1234"));

        } catch (Exception e) {
            System.err.println("Erro na carga inicial: " + e.getMessage());
        }
    }
}