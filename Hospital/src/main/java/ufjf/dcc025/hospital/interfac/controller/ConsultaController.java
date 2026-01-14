package ufjf.dcc025.hospital.interfac.controller;
/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/

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