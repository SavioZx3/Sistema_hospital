package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public interface DocumentoMedico {

    Medico getMedico();

    Paciente getPaciente();

    LocalDate getDataEmissao();
}