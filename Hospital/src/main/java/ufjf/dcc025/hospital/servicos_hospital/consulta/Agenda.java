
package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Agenda {

    private final Medico medico;
    private final List<Consulta> consultas;

    public Agenda(Medico medico) {
        if (medico == null)
            throw new DadosInvalidosException("Médico inválido.");

        this.medico = medico;
        this.consultas = new ArrayList<>();
    }

    public List<Consulta> getConsultas() {
        return new ArrayList<>(consultas);
    }

    public void marcarConsulta(Consulta consulta, Paciente paciente) throws DadosInvalidosException {
        if (consulta == null) 
            throw new DadosInvalidosException("Consulta inválida");
        
        if(consulta.getPaciente() != null || consulta.getStatus() != StatusConsulta.DISPONIVEL)
            throw new DadosInvalidosException("Consulta já está agendada");

        if (consulta.getDataHoraInicio().isBefore(LocalDateTime.now())) 
            throw new DadosInvalidosException("Não é possível marcar uma consulta em data passada");

        if (!consultas.contains(consulta)) 
            throw new DadosInvalidosException("Consulta não pertence a esta agenda");
        
        consulta.agendar(paciente);
    }

    public void gerarConsultasProximaSemana() {
        List<DisponibilidadeDiaSemana> disponibilidades = medico.getDisponibilidadesProximaSemana();

        if (disponibilidades.isEmpty())
            return;

        LocalDate proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoDomingo = proximaSegunda.plusDays(6);

        // Uso de Iterator para remover elementos da lista de forma segura
        Iterator<Consulta> it = consultas.iterator();
        while (it.hasNext()) {
            Consulta c = it.next();
            LocalDate dataC = c.getData();
            // Verifica se a data da consulta está no intervalo da próxima semana
            if (!dataC.isBefore(proximaSegunda) && !dataC.isAfter(proximoDomingo)) {
                it.remove();
            }
        }

        for (DisponibilidadeDiaSemana d : disponibilidades) {
            gerarConsultasDoDia(d, proximaSegunda);
        }
    }

    private void gerarConsultasDoDia(DisponibilidadeDiaSemana d, LocalDate proximaSegunda) {
        LocalDate dataConsulta = proximaSegunda.with(d.getDiaSemana());
        LocalTime inicio = d.getHorarioInicio();
        LocalTime fim = d.getHorarioFim();

        while (!inicio.plus(d.getDuracaoConsulta()).isAfter(fim)) {
            LocalTime horarioTermino = inicio.plus(d.getDuracaoConsulta());
            
            // Cria a consulta - o construtor da Consulta chamará o isDataDisponivel abaixo
            Consulta consulta = new Consulta(dataConsulta, inicio, horarioTermino, medico);

            consultas.add(consulta);
            inicio = horarioTermino;
        }
    }

    public List<Consulta> listarConsultasDoDia(LocalDate data) {
        List<Consulta> consultasDoDia = new ArrayList<>();
        for (Consulta c : consultas) {
            if(c.getData().equals(data)) {
                consultasDoDia.add(c);
            }
        }
        return consultasDoDia;
    }

    /**
     * Implementado: Verifica se há sobreposição de horários.
     */
    public boolean isDataDisponivel(LocalDate data, LocalTime horarioInicio, LocalTime horarioFim) {
        for (Consulta c : consultas) {
            // Se for no mesmo dia, verifica sobreposição de horários
            if (c.getData().equals(data)) {
                // Existe sobreposição se:
                // O novo início for antes do fim de uma existente E o novo fim for depois do início de uma existente
                if (horarioInicio.isBefore(c.getDataHoraFim().toLocalTime()) && 
                    horarioFim.isAfter(c.getDataHoraInicio().toLocalTime())) {
                    return false; // Horário ocupado
                }
            }
        }
        return true; // Horário livre
    }
    
    public void cancelarConsultaNaAgenda(Consulta consulta) throws IllegalStateException {
        if (consulta == null) {
            throw new DadosInvalidosException("Consulta inválida para cancelamento.");
        }

        // A própria classe Consulta já valida se o status permite cancelamento
        // (Lança IllegalStateException se não estiver AGENDADA)
        consulta.cancelarConsulta(); 
    }
    
    public void adicionarConsultaManual(Consulta consulta) {
        if (consulta != null) {
            this.consultas.add(consulta);
        }
    }
}
