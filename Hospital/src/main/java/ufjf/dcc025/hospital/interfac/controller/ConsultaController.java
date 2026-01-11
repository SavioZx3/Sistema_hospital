package ufjf.dcc025.hospital.interfac.controller;

import java.time.LocalDate;
import java.util.List;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class ConsultaController {

    public String[][] listarAgendaMedico(Medico medico) {
        
        List<Consulta> consultas = medico.getAgenda().listarConsultasDoDia(LocalDate.now());
        
        String[][] matriz = new String[consultas.size()][2];

        for (int i = 0; i < consultas.size(); i++) {
            Consulta c = consultas.get(i);
            // Se o paciente for nulo, aparece "Disponível"
            matriz[i][0] = (c.getPaciente() != null) ? c.getPaciente().getNome() : "DISPONÍVEL";
            matriz[i][1] = c.getDataHoraInicio().toLocalTime().toString();
        }
        return matriz;
    }

    // Os métodos agendar e cancelar agora chamam o hospital.agendarConsultaNaAgenda()
}
