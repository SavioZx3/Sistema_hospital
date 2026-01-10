package ufjf.dcc025.hospital.usuarios.medico;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class DisponibilidadeDiaSemana {

    private final DayOfWeek diaSemana;
    private final LocalTime horarioInicio;
    private final LocalTime horarioFim;
    private final Duration duracaoConsulta;
    private final LocalDate dataInicioVigencia;

    public DisponibilidadeDiaSemana(DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim, Duration duracaoConsulta, LocalDate dataInicioVigencia) throws DadosInvalidosException {

        if (diaSemana == null)
            throw new DadosInvalidosException("Dia da semana inválido.");

        if (horarioInicio == null || horarioFim == null || !horarioInicio.isBefore(horarioFim))
            throw new DadosInvalidosException("Horário inválido.");

        if (duracaoConsulta == null || duracaoConsulta.isZero() || duracaoConsulta.isNegative())
            throw new DadosInvalidosException("Duração da consulta inválida.");

        if (duracaoConsulta.compareTo(Duration.between(horarioInicio, horarioFim)) > 0)
            throw new DadosInvalidosException("Duração da consulta maior que o período disponível.");

        if (dataInicioVigencia == null)
            throw new DadosInvalidosException("Data de início de vigência inválida.");

        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.duracaoConsulta = duracaoConsulta;
        this.dataInicioVigencia = dataInicioVigencia;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public Duration getDuracaoConsulta() {
        return duracaoConsulta;
    }

    public LocalDate getDataInicioVigencia() {
        return dataInicioVigencia;
    }

    public boolean estaVigenteEm(LocalDate data) {
        return !data.isBefore(dataInicioVigencia);
    }

    public Duration getCargaHorariaDia() {
        return Duration.between(horarioInicio, horarioFim);
    }

    public int getQuantidadeSlots() {
        return (int) (getCargaHorariaDia().toMinutes() / duracaoConsulta.toMinutes());
    }
}
