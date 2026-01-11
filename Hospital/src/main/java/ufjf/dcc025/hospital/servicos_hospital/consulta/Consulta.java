package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Representa uma consulta médica no sistema hospitalar.
 * A consulta associa um médico, um paciente e um horário específico,
 * além de controlar seu estado (realizada, presença do paciente).
 */
public class Consulta {

    private final LocalDate data;
    private final LocalTime horarioInicio;
    private final LocalTime horarioFim;

    private final Medico medico;
    private Paciente paciente;

    private Atestado atestado;
    private Receita receita;
    private Exame exame;

    private StatusConsulta status;

    public Consulta( LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, Medico medico) throws DadosInvalidosException, DataInvalidaException {
        if(medico == null)
            throw new DadosInvalidosException("O medico responsavél pela consulta deve ser informado!");

        if(!medico.isAtivo())
            throw new DadosInvalidosException("O médico informado está inativo no sistema.");

        if(data == null || data.isBefore(LocalDate.now()))
            throw new DataInvalidaException("Data inválida.");

        if (horarioInicio == null || horarioFim == null)
            throw new DadosInvalidosException("Horário de início e fim devem ser informados.");

        if (!horarioFim.isAfter(horarioInicio))
            throw new DadosInvalidosException("O horário de fim deve ser posterior ao horário de início.");

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
    public LocalDate getData() {
        return data;
    }
    public LocalDateTime getDataHoraInicio() {
        return horarioInicio.atDate(data);
    }

    public LocalDateTime getDataHoraFim() {
        return horarioFim.atDate(data);
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public Atestado getAtestado(){
        return atestado;
    }

    public Receita getReceita(){
        return receita;
    }

    public Exame getExame(){
        return exame;
    }

    //================================= Métodos de status =================================//

    private void marcarDisponivel() {
        this.status = StatusConsulta.DISPONIVEL;
    }

    private boolean isDisponivel() {
        return this.status == StatusConsulta.DISPONIVEL;
    }
    
    public void agendar(Paciente paciente) throws DadosInvalidosException, IllegalStateException {
        if(!isDisponivel())
            throw new IllegalStateException("Só é possível agendar uma consulta que está disponível.");

        if(paciente == null)
            throw new DadosInvalidosException("Paciente inválido para agendamento de consulta.");
        
        this.paciente = paciente;
        this.status = StatusConsulta.AGENDADA;
        this.medico.getAgenda().notificarAgendamento();
    }
    
    private boolean isAgendada() {
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

    private boolean isEmAndamento() {
        return this.status == StatusConsulta.EM_ANDAMENTO;
    }

    public void marcarConcluida() throws IllegalStateException {
        if(!isEmAndamento())
            throw new IllegalStateException("Só é possível concluir uma consulta que está em andamento.");

        this.status = StatusConsulta.REALIZADA;
    }

    private boolean isRealizada() {
        return this.status == StatusConsulta.REALIZADA;
    }

    public void registrarFalta() throws IllegalStateException {
        if(!isAgendada())
            throw new IllegalStateException("Só é possível registrar falta em uma consulta que foi agendada.");

        if(LocalDateTime.now().isBefore(getDataHoraFim()))
            throw new IllegalStateException("Só é possível registrar falta após o horário de término da consulta.");


        this.status = StatusConsulta.FALTA;
        this.medico.getAgenda().notificarFalta();
    }

    public void cancelarConsulta() throws IllegalStateException {
        if(!isAgendada())
            throw new IllegalStateException("Só é possível cancelar uma consulta que foi agendada.");

        this.paciente = null;
        marcarDisponivel();
        this.medico.getAgenda().notificarCancelamento();
    }


    //================================= Métodos de recursos da consulta =================================//

    public void emitirAtestado(int duracaoDias) throws IllegalStateException, DadosInvalidosException, DataInvalidaException {
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Só é possível emitir atestado se a consulta foi realizada ou se ela está em andamento.");

        if(possuiAtestado())
            throw new IllegalStateException("Esta consulta já possui um atestado emitido");

        this.atestado = new Atestado(medico, paciente, LocalDate.now(), duracaoDias);
    }

    private boolean possuiAtestado(){
        return atestado != null;
    }

    public void emitirReceita(String medicamento , String quantidade , LocalDate validade) throws IllegalStateException, DadosInvalidosException, DataInvalidaException {
        
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Só é possível emitir receita se a consulta foi realizada e o paciente compareceu.");

        if(possuiReceita())
            throw new IllegalStateException("Esta consulta já possui uma receita emitida");

        this.receita = new Receita(medicamento, quantidade, paciente, medico, LocalDate.now(), validade);
    }

    private boolean possuiReceita(){
        return receita != null;
    }

    public void emitirExame(String tipoExame, String resultado) throws IllegalStateException{
        
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Só é possível solicitar exame se a consulta foi realizada e o paciente compareceu.");

        if(possuiExame())
            throw new IllegalStateException("Esta consulta já possui um exame solicitado");

        this.exame = new Exame(tipoExame, LocalDate.now(), medico, paciente, resultado);
    }
    
    private boolean possuiExame(){
        return exame != null;
    }
}