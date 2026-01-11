package ufjf.dcc025.hospital;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ufjf.dcc025.hospital.data.BancoDados;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.UsuarioNaoEncontradoException;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Agenda;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Classe central do sistema hospitalar.
 * Responsável por gerenciar cadastro e autenticação de usuários, controle de médicos e pacientes, agendamento, cancelamento e finalização de consultas
 * Atua como uma fachada do sistema, concentrando as regras principais de funcionamento do hospital.
 */

public class Hospital {

    private final List<Usuario> usuarios;
    private final List<Consulta> consultas;

    public Hospital() {
        this.usuarios = BancoDados.usuarios;
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
        //A unicidade do médico já esta garantida pelos validadores da classe Cpf e Email
        usuarios.add(medico);
    }

    /**
     * Cadastra um paciente no sistema.
     *
     * @param paciente paciente a ser cadastrado
     * @throws DadosInvalidosException se já existir um usuário com o mesmo CPF
     */
    public void cadastrarPaciente(Paciente paciente) {
        //De igual forma a unicidade do paciente também está garantida
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
        // Removemos caracteres especiais ( pontos e traços
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        for (Usuario u : usuarios) {
            if (u instanceof Medico && u.getCpf().equals(cpfLimpo)) {
                return (Medico) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Médico não encontrado");
    }

    /**
     * Busca um paciente pelo CPF.
     *
     * @param cpf CPF do paciente
     * @return paciente encontrado
     * @throws UsuarioNaoEncontradoException se não existir paciente com o CPF informado
     */
    public Paciente buscarPacientePorCpf(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        for (Usuario u : usuarios) {
            if (u instanceof Paciente && u.getCpf().equals(cpfLimpo)) {
                return (Paciente) u;
            }
        }
        throw new UsuarioNaoEncontradoException("Paciente não encontrado");
    }

    //LISTAGEM DOS USUÁRIOS NO SISTEMA
    /**
     * Lista todos os médicos cadastrados.
     *
     * @return lista de médicos
     */
    public List<Medico> listarMedicos() {
        
        List<Medico> listaMedicos = new ArrayList<>();
        
        for (Usuario u : usuarios) {
            if (u.getTipo().trim().toUpperCase().equals("MEDICO")) {
                listaMedicos.add((Medico) u);
            }
        }
        return new ArrayList<>(listaMedicos); //retorna uma copia da lista 
    }

    /**
     * Lista todos os pacientes cadastrados.
     *
     * @return lista de pacientes
     */
    public List<Paciente> listarPacientes() {
        
        List<Paciente> listaPacientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getTipo().trim().toUpperCase().equals("PACIENTE") ) {
                listaPacientes.add((Paciente) u);
            }
        }
         return new ArrayList<>(listaPacientes); //retorna uma copia da lista
    }

    //AUTENTICAÇÃO DOS USUÁRIOS NO SISTEMA
    /**
     * Realiza o login de um usuário no sistema.
     *
     * @param email login do usuário
     * @param senha senha do usuário
     * @return usuário autenticado
     * @throws UsuarioNaoEncontradoException se o login não existir
     * @throws DadosInvalidosException se a senha estiver incorreta
     */

    public Usuario autenticar(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                if (u.validarSenha(senha)) {
                    return u;
                }
                throw new DadosInvalidosException("Senha incorreta");
            }
        }
        throw new UsuarioNaoEncontradoException("Usuário não encontrado");
    }

}