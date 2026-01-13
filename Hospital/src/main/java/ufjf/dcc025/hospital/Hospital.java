package ufjf.dcc025.hospital;

import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.data.BancoDados;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.persistencia.HospitalData;
import ufjf.dcc025.hospital.persistencia.Persistencia;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Hospital {

    private final HospitalData dados;

    public Hospital() {
        this.dados = Persistencia.carregar();

        //  Reconstruindo referências das Consultas 
        for (Consulta c : dados.getConsultas()) {
            c.reconstruirReferencias(dados.getMedicos(), dados.getPacientes());
        }

        //  Reconstruindo referências de todos os Documentos Médicos nos Históricos
        for (Paciente p : dados.getPacientes()) {
            // Reconstruindo Atestados
            for (Atestado a : p.getHistorico().getAtestados()) {
                a.reconstruir(dados.getMedicos(), dados.getPacientes());
            }
            // Reconstruindo Exames
            for (Exame e : p.getHistorico().getExames()) {
                e.reconstruir(dados.getMedicos(), dados.getPacientes());
            }
            // Reconstruindo Receitas
            for (Receita r : p.getHistorico().getReceitas()) {
                r.reconstruir(dados.getMedicos(), dados.getPacientes());
            }
        }

        sincronizarConsultas();
    }

    private void sincronizarConsultas() {
        dados.getConsultas().clear();

        for (Medico m : dados.getMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (!dados.getConsultas().contains(c)) {
                    dados.getConsultas().add(c);
                }
            }
        }
    }

    // ========================== GESTÃO DE DOCUMENTOS ==========================
    public void salvarAlteracoes() {
        Persistencia.salvar(dados);
    }

    // ========================== BUSCAS E GESTAO ==========================
    private List<Usuario> getTodosUsuarios() {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(dados.getMedicos());
        todos.addAll(dados.getPacientes());
        todos.addAll(dados.getRecepcionistas());
        return todos;
    }

    public Paciente buscarPacientePorCpf(String cpf) {
        if (cpf == null) {
            throw new DadosInvalidosException("CPF não pode ser nulo.");
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        for (Usuario u : getTodosUsuarios()) {
            if (u instanceof Paciente && u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                return (Paciente) u;
            }
        }

        throw new UsuarioNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado.");
    }

    public Medico buscarMedicoPorCpf(String cpf) {
        if (cpf == null) {
            throw new DadosInvalidosException("CPF não pode ser nulo.");
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        for (Usuario u : getTodosUsuarios()) {
            if (u instanceof Medico && u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                return (Medico) u;
            }
        }

        throw new UsuarioNaoEncontradoException("Médico com CPF " + cpf + " não encontrado.");
    }

    // ========================== GESTÃO DE USUÁRIOS ==========================
    public void cadastrarMedico(Medico medico) {
        validarUnicidadeCpf(medico.getCpf());
        dados.getMedicos().add(medico);
        sincronizarConsultas();
        Persistencia.salvar(dados);
    }

    public void cadastrarPaciente(Paciente paciente) {
        validarUnicidadeCpf(paciente.getCpf());
        dados.getPacientes().add(paciente);
        Persistencia.salvar(dados);
    }

    private void validarUnicidadeCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        for (Usuario u : getTodosUsuarios()) {
            if (u.getCpf().replaceAll("[^0-9]", "").equals(cpfLimpo)) {
                throw new DadosInvalidosException("Já existe um usuário cadastrado com este CPF.");
            }
        }
    }

    // ========================== GESTÃO DE CONSULTAS ==========================
    public void agendarConsultaNaAgenda(Paciente paciente, Medico medico, Consulta consulta) {
        medico.getAgenda().marcarConsulta(consulta, paciente);
        paciente.getHistorico().adicionaConsulta(consulta);

        if (!dados.getConsultas().contains(consulta)) {
            dados.getConsultas().add(consulta);
        }

        salvarAlteracoes();
    }

    public void atualizarConsultasHospital(Medico medico) {
        for (Consulta c : medico.getAgenda().getConsultas()) {
            if (!dados.getConsultas().contains(c)) {
                dados.getConsultas().add(c);
            }
        }
    }

    public List<Consulta> getConsultasGerais() {
        // Atualiza a lista de consultas do HospitalData com todas as consultas dos médicos
        for (Medico m : listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (!dados.getConsultas().contains(c)) {
                    dados.getConsultas().add(c);
                }
            }
        }

        // Retorna uma cópia para evitar manipulação direta da lista interna
        return new ArrayList<>(dados.getConsultas());
    }

    // ========================== AUTENTICAÇÃO E LISTAGEM ==========================
    public Usuario autenticar(String email, String senha) {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(dados.getMedicos());
        todos.addAll(dados.getPacientes());
        todos.addAll(dados.getRecepcionistas());

        for (Usuario u : todos) {
            if (u.getEmail().getEmail().equalsIgnoreCase(email.trim())) {
                if (u.validarSenha(senha)) {
                    return u;
                }
                throw new DadosInvalidosException("Senha incorreta.");
            }
        }
        throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
    }

    public List<Medico> listarMedicos() {
        return dados.getMedicos();
    }

    public List<Paciente> listarPacientes() {
        return dados.getPacientes();
    }

    // ========================== CANCELAMENTO ==========================
    public void cancelarAgendamento(Consulta consulta) {
        consulta.getMedico().getAgenda().cancelarConsultaNaAgenda(consulta);
        dados.getConsultas().remove(consulta);

        if (consulta.getPaciente() != null) {
            consulta.getPaciente().getHistorico().getConsultas().remove(consulta);
        }

        Persistencia.salvar(dados);
    }

    public List<Consulta> getConsultas() {
        return new ArrayList<>(dados.getConsultas());
    }

}
