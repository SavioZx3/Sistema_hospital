package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Agenda {

    private final Medico medico; // Médico dono da agenda
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

        LocalDateTime agora = LocalDateTime.now();

        if (consulta.getDataHoraInicio().isBefore(agora)) 
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

        // Remove consultas futuras da próxima semana (se houver)
        consultas.removeIf(c ->
            !c.getData().isBefore(proximaSegunda) &&
            !c.getData().isAfter(proximaSegunda.plusDays(6))
        );

        for (DisponibilidadeDiaSemana d : disponibilidades) {
            gerarConsultasDoDia(d, proximaSegunda);
        }
    }

    private void gerarConsultasDoDia(DisponibilidadeDiaSemana d, LocalDate proximaSegunda) {
        LocalDate dataConsulta = proximaSegunda.with(d.getDiaSemana());
        LocalTime inicio = d.getHorarioInicio();
        LocalTime fim = d.getHorarioFim();

        while (!inicio.plus(d.getDuracaoConsulta()).isAfter(fim)) {

            Consulta consulta = new Consulta(dataConsulta, inicio, inicio.plus(d.getDuracaoConsulta()), medico);

            consultas.add(consulta);
            inicio = inicio.plus(d.getDuracaoConsulta());
        }
    }








    /**
     * Lista todas as consultas de um dia específico.
     *
     * @param data data desejada
     * @return lista de consultas do dia
     */
    public List<Consulta> listarConsultasDoDia(LocalDate data) {
        
        List<Consulta> consultasDoDia = new ArrayList<>();
        
        for (Consulta c : consultas) {
            if(c.getDataHora().toLocalDate().equals(data))
                consultasDoDia.add(c);
        }
       return new ArrayList<>(consultasDoDia);
    }

    public boolean isDataDisponivel(LocalDate data, LocalTime horarioInicio, LocalTime horarioFim) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDataDisponivel'");
    }
}