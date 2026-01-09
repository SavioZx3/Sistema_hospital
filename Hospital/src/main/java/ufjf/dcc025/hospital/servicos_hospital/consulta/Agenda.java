package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Agenda {

    private List<Consulta> consultas;

    public Agenda() {
        this.consultas = new ArrayList<>();
    }

    public List<Consulta> getConsultas() {
        return consultas;
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

    public void cancelarConsulta(Consulta consulta) {
        if (!consultas.remove(consulta)) {
            throw new UsuarioNaoEncontradoException("Consulta não encontrada");
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