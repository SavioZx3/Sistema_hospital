package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.servicos_hospital.Atestado;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Representa uma consulta médica no sistema hospitalar.
 * A consulta associa um médico, um paciente e um horário específico,
 * além de controlar seu estado (realizada, presença do paciente).
 */
public class Consulta {

    private LocalDate data;
    private LocalTime horarioInicio;

    private Medico medico;
    private Paciente paciente;

    private Atestado atestado;

    private StatusConsulta status;

    public Consulta( LocalDate data, LocalTime horarioInicio, Medico medico) {
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.medico = medico;

        this.paciente = null;
        this.status = StatusConsulta.DISPONIVEL;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHoraInicio() {
        return horarioInicio.atDate(data);
    }

    protected void agendar(Paciente paciente) {
        this.paciente = paciente;
        this.status = StatusConsulta.AGENDADA;
    }


    public boolean isRealizada() {
        if(this.status == StatusConsulta.REALIZADA)
            return true;
        else
            return false;
    }

    public void marcarConcluida() {
        this.status = StatusConsulta.REALIZADA;
    }

    public boolean isAusente() {
        if(this.status == StatusConsulta.FALTA)
            return true;
        else
            return false;
    }

    public void registrarFalta() {
        this.status = StatusConsulta.FALTA;
    }

    public void emitirAtestado(int duracaoDias) throws IllegalStateException, DadosInvalidosException, DataInvalidaException {
        if(this.status != StatusConsulta.REALIZADA && this.status != StatusConsulta.EM_ANDAMENTO)
            throw new IllegalStateException("Só é possível emitir atestado se a consulta foi realizada e o paciente compareceu.");

        if(possuiAtestado())
            throw new IllegalStateException("Esta consulta já possui um atestado emitido");

        this.atestado = new Atestado(medico, paciente, LocalDate.now(), duracaoDias);
    }

    public boolean possuiAtestado(){
        return atestado != null;
    }
    public Atestado getAtestado(){
        return atestado;
    }

    public StatusConsulta getStatus() {
        return status;
    } 
}