package ufjf.dcc025.hospital;

import java.time.LocalDate; //representa apenas a data , sem horário
import java.time.LocalTime; //representa apenas o horário , sem data
import java.time.LocalDateTime; //representa data e horário juntos

import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.servicos_hospital.Consulta;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Agenda;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Classe central do sistema hospitalar.
 * Responsável por gerenciar cadastro e autenticação de usuários, controle de médicos e pacientes, agendamento, cancelamento e finalização de consultas
 * Atua como uma fachada do sistema, concentrando as regras principais de funcionamento do hospital.
 */

public class Hospital {

    /** Lista de usuários cadastrados no sistema (médicos e pacientes) */
    private List<Usuario> usuarios;

    /** Lista de consultas registradas no hospital */
    private List<Consulta> consultas;

    /**
     * Construtor padrão do hospital.
     * Inicializa as listas de usuários e consultas.
     */
    public Hospital() {
        this.usuarios = new ArrayList<>();
        this.consultas = new ArrayList<>();
    }

    //CADASTRO DOS USUÁRIOS NO SISTEMA
    /**
     * Cadastra um médico no sistema.
     *
     * @param medico médico a ser cadastrado
     * @throws DadosInvalidosException se já existir um usuário com o mesmo CPF
     */
    public void cadastrarMedico(Medico medico) {
        boolean existe = usuarios.stream()
                .anyMatch(u -> u.getCpf().equals(medico.getCpf()));

        if (existe) {
            throw new DadosInvalidosException("Médico já cadastrado com esse CPF");
        }

        usuarios.add(medico);
    }

    /**
     * Cadastra um paciente no sistema.
     *
     * @param paciente paciente a ser cadastrado
     * @throws DadosInvalidosException se já existir um usuário com o mesmo CPF
     */
    public void cadastrarPaciente(Paciente paciente) {
        boolean existe = usuarios.stream()
                .anyMatch(u -> u.getCpf().equals(paciente.getCpf()));

        if (existe) {
            throw new DadosInvalidosException("Paciente já cadastrado com esse CPF");
        }

        usuarios.add(paciente);
    }

    //BUSCA DOS USUÁRIOS NO SISTEMA
    /**
     * Busca um médico pelo CPF.
     *
     * @param cpf CPF do médico
     * @return médico encontrado
     * @throws UsuarioNaoEncontradoException se não existir médico com o CPF informado
     */
    public Medico buscarMedicoPorCpf(String cpf) {
        return usuarios.stream()
                .filter(u -> u instanceof Medico)
                .map(u -> (Medico) u)
                .filter(m -> m.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Médico não encontrado"));
    }

    /**
     * Busca um paciente pelo CPF.
     *
     * @param cpf CPF do paciente
     * @return paciente encontrado
     * @throws UsuarioNaoEncontradoException se não existir paciente com o CPF informado
     */
    public Paciente buscarPacientePorCpf(String cpf) {
        return usuarios.stream()
                .filter(u -> u instanceof Paciente)
                .map(u -> (Paciente) u)
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Paciente não encontrado"));
    }

    //LISTAGEM DOS USUÁRIOS NO SISTEMA
    /**
     * Lista todos os médicos cadastrados.
     *
     * @return lista de médicos
     */
    public List<Medico> listarMedicos() {
        return usuarios.stream()
                .filter(u -> u instanceof Medico)
                .map(u -> (Medico) u)
                .toList();
    }

    /**
     * Lista todos os pacientes cadastrados.
     *
     * @return lista de pacientes
     */
    public List<Paciente> listarPacientes() {
        return usuarios.stream()
                .filter(u -> u instanceof Paciente)
                .map(u -> (Paciente) u)
                .toList();
    }

    //AUTENTICAÇÃO DOS USUÁRIOS NO SISTEMA
    /**
     * Realiza o login de um usuário no sistema.
     *
     * @param login login do usuário
     * @param senha senha do usuário
     * @return usuário autenticado
     * @throws UsuarioNaoEncontradoException se o login não existir
     * @throws DadosInvalidosException se a senha estiver incorreta
     */
    public Usuario login(String login, String senha) {
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.validarSenha(senha)) {
            throw new DadosInvalidosException("Senha incorreta");
        }

        return usuario;
    }

    //GERENCIAMENTO DE CONSULTAS NO HOSPITAL
    /**
     * Agenda uma nova consulta no hospital.
     *
     * @param cpfMedico CPF do médico
     * @param cpfPaciente CPF do paciente
     * @param data data da consulta
     * @param hora horário da consulta
     */
    public void marcarConsulta(String cpfMedico, String cpfPaciente, LocalDate data, LocalTime hora) {
        // LocalDate representa apenas o dia da consulta (ex: 2025-12-30)
        // LocalTime representa apenas o horário da consulta (ex: 14:30)
        // Separando facilita a validação da agenda do médico
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
        return consultas.stream()
                // Extrai apenas a data do LocalDateTime da consulta
                // para comparar com o dia informado
                .filter(c -> c.getDataHora().toLocalDate().equals(data))
                .toList();
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

        boolean conflito = consultas.stream().anyMatch(c ->
                c.getMedico().equals(medico)
                        && c.getDataHora().toLocalDate().equals(data)
                        && c.getDataHora().toLocalTime().equals(hora));

        if (conflito) {
            throw new DadosInvalidosException(
                    "Médico já possui consulta nesse horário");
        }
    }
}
