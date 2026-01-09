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
    private LocalTime horarioFim;

    private Medico medico;
    private Paciente paciente;

    private Atestado atestado;

    private StatusConsulta status;

    public Consulta( LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, Medico medico) throws DadosInvalidosException, DataInvalidaException {
        if(medico == null)
            throw new DadosInvalidosException("O medico responsavél pela consulta deve ser informado!");

        if(!medico.isAtivo())
            throw new DadosInvalidosException("O médico informado está inativo no sistema.");

        if(!medico.getAgenda1().isDataDisponivel(data, horarioInicio, horarioFim))
            throw new DadosInvalidosException("O médico não está disponível neste dia e horário.");

        if(data == null || data.isBefore(LocalDate.now()))
            throw new DataInvalidaException("Data de emissão inválida ");

        if (horarioInicio == null || horarioFim == null)
            throw new DadosInvalidosException("Horário de início e fim devem ser informados");

        if (!horarioFim.isAfter(horarioInicio))
            throw new DadosInvalidosException("O horário de fim deve ser posterior ao horário de início");

    this.data = data;
    this.horarioInicio = horarioInicio;
    this.horarioFim = horarioFim;
    this.medico = medico;

    this.paciente = null;
    this.status = StatusConsulta.DISPONIVEL;
    }


    //================================= Getters =================================//

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHoraInicio() {
        return horarioInicio.atDate(data);
    }

    public LocalDateTime getDataHoraFim() {
        return horarioFim.atDate(data);
    }


    //================================= Métodos de status =================================//

    private void marcarDisponivel() {
        this.status = StatusConsulta.DISPONIVEL;
    }

    public boolean isDisponivel() {
        return this.status == StatusConsulta.DISPONIVEL;
    }
    
    protected void agendar(Paciente paciente) throws DadosInvalidosException, IllegalStateException {
        if(!isDisponivel())
            throw new IllegalStateException("Só é possível agendar uma consulta que está disponível.");

        if(paciente == null)
            throw new DadosInvalidosException("Paciente inválido para agendamento de consulta.");
        
        this.paciente = paciente;
        this.status = StatusConsulta.AGENDADA;
    }
    
    public boolean isAgendada() {
        return this.status == StatusConsulta.AGENDADA;  
    }

    public void marcarInicioConsulta() throws IllegalStateException {
        if(!isAgendada())
            throw new IllegalStateException("Só é possível iniciar uma consulta que foi agendada.");

        if(LocalDateTime.now().isBefore(getDataHoraInicio()))
            throw new IllegalStateException("Só é possível iniciar a consulta no horário agendado ou após ele.");

        if(LocalDateTime.now().isAfter(getDataHoraFim()))
            throw new IllegalStateException("Não é possível iniciar a consulta após o horário de término.");

        this.status = StatusConsulta.EM_ANDAMENTO;
    }

    public boolean isEmAndamento() {
        return this.status == StatusConsulta.EM_ANDAMENTO;
    }

    public void marcarConcluida() throws IllegalStateException {
        if(!isEmAndamento())
            throw new IllegalStateException("Só é possível concluir uma consulta que está em andamento.");

        this.status = StatusConsulta.REALIZADA;
    }

    public boolean isRealizada() {
        return this.status == StatusConsulta.REALIZADA;
    }

    public void registrarFalta() throws IllegalStateException {
        if(!isAgendada())
            throw new IllegalStateException("Só é possível registrar falta em uma consulta que foi agendada.");

        if(LocalDateTime.now().isBefore(getDataHoraFim()))
            throw new IllegalStateException("Só é possível registrar falta após o horário de término da consulta.");


        this.status = StatusConsulta.FALTA;
    }

    public boolean isAusente() {
        return this.status == StatusConsulta.FALTA;
    }

    public void cancelarConsulta() throws IllegalStateException {
        if(!isAgendada())
            throw new IllegalStateException("Só é possível cancelar uma consulta que foi agendada.");

        this.paciente = null;
        marcarDisponivel();
    }


    //================================= Métodos de recursos da consulta =================================//

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