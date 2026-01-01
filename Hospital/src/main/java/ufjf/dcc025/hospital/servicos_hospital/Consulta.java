package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.usuarios.médico.Medico;
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
     * Recebe LocalDate e LocalTime separadamente para facilitar
     * validações externas (ex: agenda do médico).
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
}
