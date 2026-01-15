package ufjf.dcc025.hospital.usuarios;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.usuarios.medico.Especialidade;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class Recepcionista extends Usuario {

    public Recepcionista(String nome, String cpf, String email, String senha) throws DadosInvalidosException {
        super(nome, cpf, email, senha);
    }

    // --- GERENCIAR CADASTRO DE PACIENTES ---
    public void cadastrarNovoPaciente(Hospital hospital, String nome, String cpf, String email, String senha, 
                                     String telefone, String cidade, String bairro, String rua, String numero) 
                                     throws DadosInvalidosException {
        
        Paciente novoPaciente = new Paciente(nome, cpf, email, senha, telefone, cidade, bairro, rua, numero);
        
        hospital.cadastrarPaciente(novoPaciente);
    }

    // GERENCIAR CORPO CLÍNICO (CADASTRO) ---
    public void cadastrarNovoMedico(Hospital hospital, String nome, String cpf, String email, String senha, 
                                   Especialidade especialidade) throws DadosInvalidosException {
        
        Medico novoMedico = new Medico(nome, cpf, email, senha, especialidade);
        
        hospital.cadastrarMedico(novoMedico);
    }

    // --- GERENCIAR CORPO CLÍNICO (STATUS) ---
    public void alternarStatusMedico(Medico medico, boolean ativo) {
        if (medico != null) {
            if (ativo) {
                medico.ativar();
            } else {
                medico.desativar();
            }
        }
    }

    // --- REQUISITO: CONTROLAR VISITAS ---
    public String verificarStatusVisita(Paciente paciente) {
        if (paciente == null) {
            return "Paciente não encontrado.";
        }
        
        // Se não estiver internado, a visita nem é processada
        if (paciente.getStatusInternacao() == StatusInternacao.NAO_INTERNADO) {
            return "O paciente " + paciente.getNome() + " não está em regime de internação no momento.";
        }
        
        // Retorna se o médico permitiu (APTO) ou não (INAPTO)
        return "Status de Visita: " + paciente.getStatusVisita().getDescricao();
    }

    @Override
    public String getTipo() {
        return "RECEPCIONISTA";
    }

    @Override
    public String toString() {
        return "Recepcionista: " + nome + " | CPF: " + getCpf();
    }
}