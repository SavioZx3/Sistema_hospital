package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class ConsultaController {
    private final Hospital hospital;

    public ConsultaController(Hospital hospital) {
        this.hospital = hospital;
    }

    public void agendar(Paciente p, Medico m, Consulta c) {
        hospital.agendarConsultaNaAgenda(p, m, c);
    }

    public void cancelar(Consulta c) {
        hospital.cancelarAgendamento(c);
    }
}