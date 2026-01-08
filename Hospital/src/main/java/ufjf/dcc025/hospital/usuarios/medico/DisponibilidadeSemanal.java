package ufjf.dcc025.hospital.usuarios.medico;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilidadeSemanal {

    private DayOfWeek diaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private Duration duracaoConsulta;

    // A partir de quando essa disponibilidade vale
    private LocalDate dataInicioVigencia;

    public DisponibilidadeSemanal(DayOfWeek diaSemana, LocalTime horarioInicio, LocalTime horarioFim, Duration duracaoConsulta, LocalDate dataInicioVigencia) {
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.duracaoConsulta = duracaoConsulta;
        this.dataInicioVigencia = dataInicioVigencia;
    }

    // getters (sem setters se quiser imutável)
}