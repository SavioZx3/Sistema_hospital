package ufjf.dcc025.hospital;

import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.data.BancoDados;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Hospital {

    private final List<Usuario> usuarios;
    private final List<Consulta> consultasGerais;

    public Hospital() {
        this.usuarios = BancoDados.usuarios;
        this.consultasGerais = new ArrayList<>();
        sincronizarDadosIniciais();
    }

    private void sincronizarDadosIniciais() {
        this.consultasGerais.clear();
        for (Usuario u : usuarios) {
            if (u instanceof Medico) {
                atualizarConsultasHospital((Medico) u);
            }
        }
    }

    // ========================== BUSCAS (ESSENCIAIS PARA A VIEW) ==========================

    public Paciente buscarPacientePorCpf(String cpf) {
        if (cpf == null) throw new DadosInvalidosException("CPF não pode ser nulo.");
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        for (Usuario u : usuarios) {
            if (u instanceof Paciente && u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                return (Paciente) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado.");
    }

    public Medico buscarMedicoPorCpf(String cpf) {
        if (cpf == null) throw new DadosInvalidosException("CPF não pode ser nulo.");
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        for (Usuario u : usuarios) {
            if (u instanceof Medico && u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                return (Medico) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Médico com CPF " + cpf + " não encontrado.");
    }

    // ========================== GESTÃO DE USUÁRIOS ==========================

    public void cadastrarMedico(Medico medico) {
        validarUnicidadeCpf(medico.getCpf());
        usuarios.add(medico);
        atualizarConsultasHospital(medico);
    }

    public void cadastrarPaciente(Paciente paciente) {
        validarUnicidadeCpf(paciente.getCpf());
        usuarios.add(paciente);
    }

    private void validarUnicidadeCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        for (Usuario u : usuarios) {
            if (u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                throw new DadosInvalidosException("Já existe um usuário cadastrado com este CPF.");
            }
        }
    }

    // ========================== GESTÃO DE CONSULTAS ==========================

    public void agendarConsultaNaAgenda(Paciente paciente, Medico medico, Consulta consultaSelecionada) {
        if (paciente == null || medico == null || consultaSelecionada == null) {
            throw new DadosInvalidosException("Dados incompletos.");
        }
        medico.getAgenda().marcarConsulta(consultaSelecionada, paciente);
        paciente.getHistorico().adicionaConsulta(consultaSelecionada);
        if (!consultasGerais.contains(consultaSelecionada)) {
            consultasGerais.add(consultaSelecionada);
        }
    }

    public void atualizarConsultasHospital(Medico medico) {
        for (Consulta c : medico.getAgenda().getConsultas()) {
            if (!consultasGerais.contains(c)) {
                consultasGerais.add(c);
            }
        }
    }

    public List<Consulta> getConsultasGerais() {
        for (Medico m : listarMedicos()) {
            atualizarConsultasHospital(m);
        }
        return new ArrayList<>(this.consultasGerais);
    }

    // ========================== AUTENTICAÇÃO E LISTAGEM ==========================

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().toString().equalsIgnoreCase(email.trim())) {
                if (u.validarSenha(senha)) return u;
                throw new DadosInvalidosException("Senha incorreta.");
            }
        }
        throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
    }
    
    public List<Medico> listarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Medico) medicos.add((Medico) u);
        }
        return medicos;
    }

    public List<Paciente> listarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Paciente) pacientes.add((Paciente) u);
        }
        return pacientes;
    }
    
    // ========================== CANCELAMENTO ==========================

    public void cancelarAgendamento(Consulta consulta) {
        if (consulta == null) {
            throw new DadosInvalidosException("Selecione uma consulta para cancelar.");
        }

        // 1. Solicita o cancelamento via agenda do médico responsável
        // Isso muda o status da consulta para CANCELADA ou DISPONIVEL na agenda dele
        if (consulta.getMedico() != null && consulta.getMedico().getAgenda() != null) {
            consulta.getMedico().getAgenda().cancelarConsultaNaAgenda(consulta);
        }

        // 2. Remove da lista global de consultas agendadas do hospital
        this.consultasGerais.remove(consulta);
        
        // 3. Opcional: Remover do histórico do paciente se for um cancelamento definitivo
        if (consulta.getPaciente() != null) {
            consulta.getPaciente().getHistorico().getConsultas().remove(consulta);
        }
    }
    
}
