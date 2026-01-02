package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Representa uma consulta médica no sistema hospitalar.
 * A consulta associa um médico, um paciente e um horário específico,
 * além de controlar seu estado (realizada, presença do paciente).
 */
public class Consulta {

    /** Médico responsável pela consulta */
    private Medico medico;

    /** Paciente atendido na consulta */
    private Paciente paciente;

    /**
     * Data e horário exatos da consulta.
     * LocalDateTime representa um instante completo (data + hora).
     */
    private LocalDateTime dataHora;

    /** Indica se a consulta já foi realizada */
    private boolean realizada;

    /** Indica se o paciente compareceu à consulta */
    private boolean compareceu;

    /** Atestado emitido para a consulta */
    private Atestado atestado;
    /**
     * Cria uma nova consulta médica.
     *
     * @param medico médico responsável
     * @param paciente paciente atendido
     * @param dataHora data e horário da consulta
     */
    public Consulta(Medico medico, Paciente paciente, LocalDateTime dataHora) {
        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.realizada = false;
        this.compareceu = false;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isRealizada() {
        return realizada;
    }

    /**
     * Marca a consulta como realizada.
     */
    public void marcarConcluida() {
        this.realizada = true;
    }

    /**
     * Reagenda a consulta para uma nova data e horário.
     */
    public void reagendar(LocalDate novaData, LocalTime novaHora) {
        this.dataHora = LocalDateTime.of(novaData, novaHora);
    }

    public boolean isCompareceu() {
        return compareceu;
    }

    /**
     * Registra a presença do paciente na consulta.
     */
    public void registrarPresenca() {
        this.compareceu = true;
    }

    /**
     * Registra a falta do paciente na consulta.
     */
    public void registrarFalta() {
        this.compareceu = false;
    }

    /**
     * Emite um atestado médico a partir desta consulta.
     * Só pode ser emitido se a consulta foi realizada e o paciente compareceu
     */
    public void emitirAtestado(String mensagem, int duracaoDias){
        if(!realizada || !compareceu){
            throw new IllegalStateException("Só é possível emitir atestado se a consulta foi realizada e o paciente compareceu.");
}

        if(this.atestado != null){
            throw new IllegalStateException("Esta consulta já possui um atestado emitido");
        }
        this.atestado = new Atestado(medico, paciente, mensagem, LocalDate.now(), duracaoDias);
        this.atestado.emitir();
    }

    public boolean possuiAtestado(){
        return atestado != null;
    }
    public Atestado getAtestado(){
        return atestado;
    } 
}