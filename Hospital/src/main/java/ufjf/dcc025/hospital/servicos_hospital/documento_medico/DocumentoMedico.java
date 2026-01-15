package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import java.time.LocalDate;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public interface DocumentoMedico {

    Medico getMedico();

    Paciente getPaciente();

    LocalDate getDataEmissao();
}