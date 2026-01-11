
package ufjf.dcc025.hospital.usuarios.paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.outros.Endereco;

public class Paciente extends Usuario {
    private List<Exame> exames; 
    private List<Receita> receitas;
    private List<Atestado> atestados;

    private String telefone;
    private Endereco endereco; 
    private StatusVisita status;
    private Historico historico;
    private StatusInternacao statusInternacao = StatusInternacao.NAO_INTERNADO;

    /**
     *
     * @param nome
     * @param cpf
     * @param email
     * @param senha
     * @param telefone
     * @param cidade
     * @param bairro
     * @param rua
     * @param numero
     * @throws DadosInvalidosException
     */
    public Paciente(String nome, String cpf, String email,String senha, String telefone, String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        super(nome, cpf, email, senha);

        if (telefone == null || telefone.trim().isEmpty()) {
            throw new DadosInvalidosException("É obrigatório um número de telefone");
        }

        this.endereco = new Endereco(cidade,bairro,rua,numero);
        this.telefone = telefone;        
        // Inicializando as listas para evitar NullPointerException
        exames = new ArrayList<>();
        receitas = new ArrayList<>();
        historico = new Historico();
        status = StatusVisita.APTO;
        atestados = new ArrayList<>();
        
    }

    public String getTelefone() { 
        return telefone; 
    }

    public void setTelefone(String telefone) throws DadosInvalidosException {
        if (telefone == null || telefone.trim().isEmpty())
            throw new DadosInvalidosException("Número de telefone vazio");
        this.telefone = telefone;
    }

    public String getEndereco() { 
        return endereco.getLogradouro(); 
    }

    public void setEndereco(String cidade, String bairro, String rua, String numero) throws DadosInvalidosException {
        this.endereco = new Endereco(cidade,bairro,rua,numero);
    }
    
    public StatusVisita getStatusVisita() {
        return status;
    }
    
    public void setStatusVisita(StatusVisita status) {
        this.status = status;
    }
    
    public Historico getHistorico() {
        return historico;
    }
    
    public void setStatusInternacao(StatusInternacao status) {
        this.statusInternacao = status;
    }
    
    public StatusInternacao getStatusInternacao() {
        return statusInternacao;
    }
    
    public List<Atestado> getAtestados() {
        return new ArrayList<>(atestados);
    }
    
    public List<Receita> getReceitas() {
        return new ArrayList<>(this.receitas);
    }

    public List<Exame> getExames() {
        return new ArrayList<>(this.exames);
    }
    
    public boolean podeReceberVisita() {
        
        return this.statusInternacao == StatusInternacao.INTERNADO && this.status == StatusVisita.APTO;
    }
    
    public void adicionarAtestado(Atestado atestado) {
        if (atestado != null) {
            this.atestados.add(atestado);
        }
    }
    
    public void adicionarReceita(Receita receita) {
        if (receita != null) {
            this.receitas.add(receita);
        }
    }
    
    public void adicionarExame(Exame exame) {
        if (exame != null) {
            this.exames.add(exame);
            this.historico.adicionaExame(exame); // Exame vai para o histórico
        }
    }
    
    @Override
    public String getTipo() {
        return "PACIENTE";
    }

    @Override
    public String toString() {
        return getTipo() + ": " + nome + " | CPF: " + getCpf();
    }
}
