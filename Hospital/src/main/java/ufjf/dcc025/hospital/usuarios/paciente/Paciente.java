package ufjf.dcc025.hospital.usuarios.paciente;

import java.util.List;
import java.util.ArrayList;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.Atestado;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.servicos_hospital.Exame;
import ufjf.dcc025.hospital.servicos_hospital.Receita;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.outros.Endereco; 

public class Paciente extends Usuario {

    private List<Atestado> atestados;
    private List<Consulta> consultas;
    private List<Exame> exames; 
    private List<Receita> receitas;

    private String telefone;
    private Endereco endereco; 
    private Historico historico;
    private StatusVisita status;

    public Paciente(String nome, String cpf, String email, String senha, Historico historico, String endereco, String telefone) throws DadosInvalidosException {
        super(nome, cpf, email, senha);

        if (telefone == null || telefone.trim().isEmpty()) {
            throw new DadosInvalidosException("É obrigatório um número de telefone");
        }

        this.endereco = new Endereco(endereco);
        this.telefone = telefone;
        this.historico = (historico != null) ? historico : new Historico();
        
        // Inicializando as listas para evitar NullPointerException
        this.atestados = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
        this.receitas = new ArrayList<>();
    }

    public Historico getHistorico() { 
        return historico; 
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
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

    public void setEndereco(String novoEndereco) throws DadosInvalidosException {
        this.endereco = new Endereco(novoEndereco);
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