package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import java.util.List;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Receita implements DocumentoMedico {

    private final LocalDate validade;
    private final LocalDate dataEmissao;
    private final String medicamento;
    private final String quantidade;
    
    private transient Paciente paciente;
    private transient Medico medico;
    private String pacienteCpf;
    private String medicoCpf;
    
    public Receita(String medicamento, String quantidade, Paciente paciente, Medico medico, LocalDate dataEmissao, LocalDate validade) throws DadosInvalidosException, DataInvalidaException {
        
        if(medicamento == null || medicamento.trim().isEmpty()) throw new DadosInvalidosException("Medicamento obrigatório");
        if(quantidade == null || quantidade.trim().isEmpty()) throw new DadosInvalidosException("Quantidade obrigatória");
        if(paciente == null || medico == null) throw new DadosInvalidosException("Paciente e Médico obrigatórios");
        if(dataEmissao == null || validade == null) throw new DataInvalidaException("Datas obrigatórias");
        if(validade.isBefore(dataEmissao)) throw new DataInvalidaException("Validade anterior à emissão");
        
        this.medicamento = medicamento;
        this.quantidade = quantidade;
        this.paciente = paciente;
        this.medico = medico;
        this.pacienteCpf = paciente.getCpf();
        this.medicoCpf = medico.getCpf();
        this.dataEmissao = dataEmissao;
        this.validade = validade;
    }
    
    public void reconstruir(List<Medico> medicos, List<Paciente> pacientes) {
        this.medico = medicos.stream().filter(m -> m.getCpf().equals(medicoCpf)).findFirst().orElse(null);
        this.paciente = pacientes.stream().filter(p -> p.getCpf().equals(pacienteCpf)).findFirst().orElse(null);
    }

    @Override public Medico getMedico() { return medico; }
    @Override public Paciente getPaciente() { return paciente; }
    @Override public LocalDate getDataEmissao() { return dataEmissao; }
    public String getMedicamento() { return medicamento; }
    public LocalDate getValidade() { return validade; }  
    
    @Override
    public String toString() {
    return """
           ==========================================
                        RECEITA MÉDICA               
           ==========================================
           MÉDICO: """ + medico.getNome() + "\n" +
           "PACIENTE: " + paciente.getNome() + "\n" +
           "------------------------------------------\n" +
           "MEDICAMENTO: " + medicamento + "\n" +
           "QUANTIDADE: " + quantidade + "\n" +
           "------------------------------------------\n" +
           "DATA DE EMISSÃO: " + dataEmissao + "\n" +
           "VALIDADE: " + validade + "\n" +
           "==========================================";
    }
}