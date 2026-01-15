package ufjf.dcc025.hospital.usuarios.paciente;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
import java.util.List;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.outros.Endereco;

public class Paciente extends Usuario {
    
    private String telefone;
    private Endereco endereco; 
    private StatusVisita status;
    private Historico historico;
    private StatusInternacao statusInternacao = StatusInternacao.NAO_INTERNADO;

    public Paciente(String nome, String cpf, String email, String senha, String telefone, String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        super(nome, cpf, email, senha);

        if (telefone == null || telefone.trim().isEmpty()) {
            throw new DadosInvalidosException("É obrigatório um número de telefone");
        }

        this.endereco = new Endereco(cidade, bairro, rua, numero);
        this.telefone = telefone;        
        
        // Inicializamos apenas o histórico, que conterá todas as listas internas
        this.historico = new Historico();
        this.status = StatusVisita.APTO;        
    }

    // ================== Getters e Setters Básicos ==================

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) throws DadosInvalidosException {
        if (telefone == null || telefone.trim().isEmpty())
            throw new DadosInvalidosException("Número de telefone vazio");
        this.telefone = telefone;
    }

    public String getLogradouro() { return endereco.getLogradouro(); }
    
    public Endereco getEndereco() { return endereco; } 

    public void setEndereco(String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        this.endereco = new Endereco(cidade, bairro, rua, numero);
    }
    
    public StatusVisita getStatusVisita() { return status; }
    
    public void setStatusVisita(StatusVisita status) { this.status = status; }
    
    public Historico getHistorico() { return historico; }
    
    public void setStatusInternacao(StatusInternacao status) { this.statusInternacao = status; }
    
    public StatusInternacao getStatusInternacao() { return statusInternacao; }


    public List<Atestado> getAtestados() {
        return historico.getAtestados();
    }
    
    public List<Receita> getReceitas() {
        return historico.getReceitas();
    }

    public List<Exame> getExames() {
        return historico.getExames();
    }
    
    public void adicionarAtestado(Atestado atestado) {
        this.historico.adicionaAtestado(atestado);
    }
    
    public void adicionarReceita(Receita receita) {
        this.historico.adicionaReceita(receita);
    }
    
    public void adicionarExame(Exame exame) {
        this.historico.adicionaExame(exame);
    }

    public boolean podeReceberVisita() {
        return this.statusInternacao == StatusInternacao.INTERNADO && this.status == StatusVisita.APTO;
    }
    
    @Override
    public String getTipo() {
        return "PACIENTE";
    }

    @Override
    public String toString() {
        return getTipo() + ": " + nome + "\n CPF: " + getCpf();
    }
}