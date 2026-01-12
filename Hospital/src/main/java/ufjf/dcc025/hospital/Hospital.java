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

/**
 * Classe central do sistema hospitalar (Fachada).
 * Concentra as regras de negócio e a persistência em memória.
 */
public class Hospital {

    private final List<Usuario> usuarios;
    private final List<Consulta> consultasGerais; // Centraliza todas as consultas do hospital

    public Hospital() {
        // Carrega a lista estática do Banco de Dados
        this.usuarios = BancoDados.usuarios;
        this.consultasGerais = new ArrayList<>();
        
        // Sincroniza as consultas dos médicos já cadastrados no BancoDados
        sincronizarDadosIniciais();
    }

    /**
     * percorre a lista de usuários e adiciona as consultas de cada médico 
     * à lista global do hospital.
     */
    private void sincronizarDadosIniciais() {
        this.consultasGerais.clear(); // Garante que a lista comece limpa
        for (Usuario u : usuarios) {
            if (u instanceof Medico) {
                atualizarConsultasHospital((Medico) u);
            }
        }
    }

    // ========================== GESTÃO DE USUÁRIOS ==========================

    public void cadastrarMedico(Medico medico) {
        validarUnicidadeCpf(medico.getCpf());
        usuarios.add(medico);
    }

    public void cadastrarPaciente(Paciente paciente) {
        validarUnicidadeCpf(paciente.getCpf());
        usuarios.add(paciente);
    }

    private void validarUnicidadeCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpfLimpo)) {
                throw new DadosInvalidosException("Já existe um usuário cadastrado com este CPF.");
            }
        }
    }

    public Medico buscarMedicoPorCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        for (Usuario u : usuarios) {
            if (u instanceof Medico && u.getCpf().equals(cpfLimpo)) {
                return (Medico) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Médico não encontrado.");
    }

    public Paciente buscarPacientePorCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        for (Usuario u : usuarios) {
            if (u instanceof Paciente && u.getCpf().equals(cpfLimpo)) {
                return (Paciente) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Paciente não encontrado.");
    }

    // ========================== GESTÃO DE CONSULTAS ==========================

    /**
     * Agendar consulta utilizando a lógica da classe Agenda do Médico.
     */
    public void agendarConsultaNaAgenda(Paciente paciente, Medico medico, Consulta consultaSelecionada) {
 
    if (paciente == null) {
        throw new DadosInvalidosException("Selecione um paciente para agendar.");
    }
    
    // Altera o estado na agenda individual do médico
    medico.getAgenda().marcarConsulta(consultaSelecionada, paciente);
    
    // Adiciona ao histórico do paciente 
    paciente.getHistorico().adicionaConsulta(consultaSelecionada);
    
    if (!consultasGerais.contains(consultaSelecionada)) {
        consultasGerais.add(consultaSelecionada);
    }
}

    /**
     * Sincroniza a lista global com novas consultas geradas na agenda de um médico.
     */
    public void atualizarConsultasHospital(Medico medico) {
        for (Consulta c : medico.getAgenda().getConsultas()) {
            if (!consultasGerais.contains(c)) {
                consultasGerais.add(c);
            }
        }
    }

    /**
     * Retorna a lista de todas as consultas do sistema.
     */
    public List<Consulta> getConsultasGerais() {
        // Força uma atualização de todos os médicos antes de retornar a lista
        for (Medico m : listarMedicos()) {
            atualizarConsultasHospital(m);
        }
        return new ArrayList<>(this.consultasGerais);
    }

    public void cancelarAgendamento(Consulta consulta) {
        if (consulta == null) {
            throw new DadosInvalidosException("Selecione uma consulta para cancelar.");
        }
        // Solicita o cancelamento via agenda do médico responsável
        consulta.getMedico().getAgenda().cancelarConsultaNaAgenda(consulta);
    }

    // ========================== AUTENTICAÇÃO E LISTAGEM ==========================

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
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
}
