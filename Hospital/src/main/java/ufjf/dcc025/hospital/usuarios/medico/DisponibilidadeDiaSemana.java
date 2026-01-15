package ufjf.dcc025.hospital.usuarios.medico;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;

public class DisponibilidadeDiaSemana {

    private final DayOfWeek diaSemana;
    private final LocalTime horarioInicio;
    private final LocalTime horarioFim;
    private final Duration duracaoConsulta;
    private LocalDate dataInicioVigencia;

    public DisponibilidadeDiaSemana(DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim, Duration duracaoConsulta) throws DadosInvalidosException {

        if (diaSemana == null)
            throw new DadosInvalidosException("Dia da semana inválido.");

        if (horarioInicio == null || horarioFim == null || !horarioInicio.isBefore(horarioFim))
            throw new DadosInvalidosException("Horário inválido.");

        if (duracaoConsulta == null || duracaoConsulta.isZero() || duracaoConsulta.isNegative())
            throw new DadosInvalidosException("Duração da consulta inválida.");

        if (duracaoConsulta.compareTo(Duration.between(horarioInicio, horarioFim)) > 0)
            throw new DadosInvalidosException("Duração da consulta maior que o período disponível.");

        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.duracaoConsulta = duracaoConsulta;
    }

    protected void definirDataInicioVigencia(LocalDate data) {
        this.dataInicioVigencia = data;
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

    public Duration getCargaHorariaDia() {
        return Duration.between(horarioInicio, horarioFim);
    }

    public int getQuantidadeSlots() {
        return (int) (getCargaHorariaDia().toMinutes() / duracaoConsulta.toMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisponibilidadeDiaSemana)) return false;

        DisponibilidadeDiaSemana d = (DisponibilidadeDiaSemana) o;
        return diaSemana == d.diaSemana &&
            horarioInicio.equals(d.horarioInicio) &&
            horarioFim.equals(d.horarioFim) &&
            duracaoConsulta.equals(d.duracaoConsulta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            diaSemana,
            horarioInicio,
            horarioFim,
            duracaoConsulta
        );
    }
}
