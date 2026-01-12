package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Agenda {
    private final Medico medico;
    private final List<Consulta> consultas;
    
    // Flags de Notificação
    private boolean houveAgendamento;
    private boolean houveCancelamento;
    private boolean houveFalta;

    public Agenda(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("Médico não pode ser nulo.");
        }
        this.medico = medico;
        this.consultas = new ArrayList<>();
    }

    // ============================ Getters ========================== //

    public List<Consulta> getConsultas() {
        return new ArrayList<>(consultas);
    }

    public List<Consulta> getConsultasMarcadas() {
        List<Consulta> marcadas = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getStatus() == StatusConsulta.AGENDADA) {
                marcadas.add(c);
            }
        }
        
        // Ordenação manual por data e hora
        Collections.sort(marcadas, new Comparator<Consulta>() {
            @Override
            public int compare(Consulta c1, Consulta c2) {
                return c1.getDataHoraInicio().compareTo(c2.getDataHoraInicio());
            }
        });
        return marcadas;
    }

    // ======================== Lógica de Negócio ===================== //

    public void adicionarConsultaManual(Consulta c) {
        if (c != null) {
            this.consultas.add(c);
        }
    }
    
    public List<Consulta> listarConsultasDoDia(LocalDate data) {
    List<Consulta> consultasDoDia = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getData().equals(data)) {
                consultasDoDia.add(c);
            }
        }

        // Opcional: Ordenar para que a tabela não fique bagunçada
        Collections.sort(consultasDoDia, new Comparator<Consulta>() {
            @Override
            public int compare(Consulta c1, Consulta c2) {
                return c1.getDataHoraInicio().compareTo(c2.getDataHoraInicio());
            }
        });

        return consultasDoDia;
    }
    
    public void marcarConsulta(Consulta consulta, Paciente paciente) throws DadosInvalidosException {
        if (consulta == null || consulta.getStatus() != StatusConsulta.DISPONIVEL) {
            throw new DadosInvalidosException("Consulta indisponível para agendamento.");
        }
        
        consulta.agendar(paciente);
        notificarAgendamento();
    }

    public void cancelarConsultaNaAgenda(Consulta consulta) {
        if (consulta != null) {
            consulta.cancelarConsulta();
            notificarCancelamento();
        }
    }

    public void gerarConsultasProximaSemana() {
        List<DisponibilidadeDiaSemana> disponibilidades = medico.getDisponibilidadesProximaSemana();
        if (disponibilidades.isEmpty()) return;

        LocalDate proximaSegunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate proximoDomingo = proximaSegunda.plusDays(6);

        // Limpeza de horários antigos disponíveis usando Iterator
        Iterator<Consulta> it = consultas.iterator();
        while (it.hasNext()) {
            Consulta c = it.next();
            LocalDate d = c.getData();
            // Remove apenas se for da próxima semana e ainda estiver livre
            if (!d.isBefore(proximaSegunda) && !d.isAfter(proximoDomingo) && c.getStatus() == StatusConsulta.DISPONIVEL) {
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
            LocalTime termino = inicio.plus(d.getDuracaoConsulta());
            if (isDataDisponivel(dataConsulta, inicio, termino)) {
                consultas.add(new Consulta(dataConsulta, inicio, termino, medico));
            }
            inicio = termino;
        }
    }

    public boolean isDataDisponivel(LocalDate data, LocalTime inicio, LocalTime fim) {
        for (Consulta c : consultas) {
            if (c.getData().equals(data)) {
                // Checa sobreposição de horários
                if (inicio.isBefore(c.getDataHoraFim().toLocalTime()) && fim.isAfter(c.getDataHoraInicio().toLocalTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    // ========================== Notificações ======================== //

    public void notificarAgendamento() { 
        this.houveAgendamento = true; 
    }

    public void notificarCancelamento() { 
        this.houveCancelamento = true; 
    }

    public void notificarFalta() { 
        this.houveFalta = true; 
    }

    public List<String> consumirNotificacoes() {
        List<String> avisos = new ArrayList<>();
        
        if (houveAgendamento) 
            avisos.add("Há novos agendamentos na sua agenda.");
        
        if (houveCancelamento) 
            avisos.add("Atenção: Algumas consultas foram canceladas.");
            
        if (houveFalta) 
            avisos.add("Aviso: Houve registro de falta de paciente.");

        // Reset das flags após leitura
        this.houveAgendamento = false; 
        this.houveCancelamento = false;
        this.houveFalta = false;
        
        return avisos;
    }
}