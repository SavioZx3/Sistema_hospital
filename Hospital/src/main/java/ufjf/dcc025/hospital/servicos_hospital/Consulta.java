package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.usuarios.medico.Agenda;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Representa uma consulta médica no sistema hospitalar.
 * A consulta associa um médico, um paciente e um horário específico,
 * além de controlar seu estado (realizada, presença do paciente).
 */
public class Consulta {

    private Medico medico;
    private Paciente paciente;
    private LocalDateTime dataHora;
    private boolean realizada;
    private boolean compareceu;


    private Atestado atestado;
    
    /**
     * @param medico médico responsável
     * @param paciente paciente atendido
     * @param dataHora data e horário da consulta
     */
    public Consulta(Medico medico, LocalDateTime dataHora) {
        this.medico = medico;
        this.paciente = null;
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

    public void marcarConcluida() {
        this.realizada = true;
    }

    public boolean isCompareceu() {
        return compareceu;
    }

    public void registrarPresenca() {
        this.compareceu = true;
    }

    public void registrarFalta() {
        this.compareceu = false;
    }

    public void reagendar(LocalDate novaData, LocalTime novaHora) {
        this.dataHora = LocalDateTime.of(novaData, novaHora);
    }

    public void emitirAtestado(String mensagem, int duracaoDias){
        if(!realizada || !compareceu){
            throw new IllegalStateException("Só é possível emitir atestado se a consulta foi realizada e o paciente compareceu.");
}
        if(possuiAtestado()){
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

    public void marcarConsulta(String cpfMedico, String cpfPaciente, LocalDate data, LocalTime hora) {
        Medico medico = buscarMedicoPorCpf(cpfMedico);
        Paciente paciente = buscarPacientePorCpf(cpfPaciente);

        if (medico.getAgenda1() == null) {
            throw new DadosInvalidosException("Médico não possui agenda definida");
        }
        // Valida se a data e o horário respeitam a agenda do médico
        validarHorarioConsulta(medico, data, hora);

        // Valida se já não existe outra consulta no mesmo horário
        validarConflitoHorario(medico, data, hora);
        // LocalDateTime combina data + horário e representa o momento exato em que a consulta ocorrerá
        LocalDateTime dataHora = LocalDateTime.of(data, hora);

        Consulta consulta = new Consulta(medico, paciente, dataHora);

        consultas.add(consulta);
    }

    /**
     * Cancela uma consulta existente.
     *
     * @param consulta consulta a ser cancelada
     */
    public void cancelarConsulta(Consulta consulta) {
        if (!consultas.remove(consulta)) {
            throw new UsuarioNaoEncontradoException("Consulta não encontrada");
        }
    }

    /**
     * Reagenda uma consulta existente.
     *
     * @param consulta consulta a ser reagendada
     * @param novaData nova data
     * @param novaHora novo horário
     */
    public void reagendarConsulta(Consulta consulta, LocalDate novaData, LocalTime novaHora) {

        if (!consultas.contains(consulta)) {
            throw new UsuarioNaoEncontradoException("Consulta não encontrada");
        }

        validarHorarioConsulta(consulta.getMedico(), novaData, novaHora);
        consulta.reagendar(novaData, novaHora);
    }

    /**
     * Finaliza uma consulta, marcando-a como realizada.
     *
     * @param consulta consulta a ser finalizada
     */
    public void finalizarConsulta(Consulta consulta) {
        if (!consultas.contains(consulta)) {
            throw new UsuarioNaoEncontradoException("Consulta não encontrada");
        }

        if (consulta.isRealizada()) {
            throw new DadosInvalidosException("Consulta já foi realizada");
        }

        consulta.marcarConcluida();
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

    //VALIDAÇÕES INTERNAS DO SISTEMA

    /**
     * Valida se o horário da consulta é compatível com a agenda do médico.
     * Usa LocalDate para validar o dia da semana e LocalTime para validar o intervalo e duração do atendimento.
     */
    private void validarHorarioConsulta(Medico medico, LocalDate data, LocalTime hora) {
        Agenda agenda = medico.getAgenda1();

        // Verifica se o médico atende no dia da semana da data informada
        if (!agenda.getDia().equals(data.getDayOfWeek())) {
            throw new DadosInvalidosException("Médico não atende neste dia");
        }

        // Verifica se o horário está dentro do período de atendimento
        if (hora.isBefore(agenda.getHoraInicio())
                || hora.isAfter(agenda.getHoraFim())) {
            throw new DadosInvalidosException("Horário fora do período de atendimento");
        }
        // Calcula a diferença em minutos entre o início da agenda e o horário informado
        long minutos = java.time.Duration
                .between(agenda.getHoraInicio(), hora)
                .toMinutes();

        // Verifica se o horário respeita a duração padrão das consultas
        if (minutos % agenda.getDuracaoConsultas() != 0) {
            throw new DadosInvalidosException(
                    "Horário incompatível com a duração da consulta");
        }
    }

    /**
     * Verifica se já existe consulta marcada no mesmo horário para o médico.
     * Utiliza LocalDateTime para comparar data e horário da consulta existente.
     */
    private void validarConflitoHorario(Medico medico, LocalDate data, LocalTime hora) {

        // Percorremos a lista de consultas uma a uma
        for (Consulta c : consultas) {
        
            // Verificamos se o médico é o mesmo
            boolean mesmoMedico = c.getMedico().equals(medico);

            // Extraí a data e a hora da consulta da lista para comparar
            boolean mesmaData = c.getDataHora().toLocalDate().equals(data);
            boolean mesmaHora = c.getDataHora().toLocalTime().equals(hora);

            // Se as três condições forem verdadeiras, há um conflito
            if (mesmoMedico && mesmaData && mesmaHora) {
                throw new DadosInvalidosException("Médico já possui consulta nesse horário");
            }
        }
    }
}