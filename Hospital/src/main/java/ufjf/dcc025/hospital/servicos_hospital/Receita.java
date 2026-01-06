package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Receita {
    
    private LocalDate validade;
    private LocalDate dataEmissao;
    
    private String medicamento;
    private String quantidade;
    private Paciente paciente;
    private Medico medico;
    
    public Receita(String medicamento , String quantidade , Paciente paciente , Medico medico , LocalDate dataEmissao , LocalDate validade) 
        throws  DadosInvalidosException{
        
        if(medicamento == null || medicamento.trim().isEmpty()){
            throw new DadosInvalidosException("É obrigatório o informar o nome do medicamento ");
        }
        
        if(quantidade == null || quantidade.trim().isEmpty()){
            throw new DadosInvalidosException("A quantidade deve ser informada! ");
        }
        
        if(paciente == null ){
            throw new DadosInvalidosException("O paciente da receita deve ser informado! ");
        }
        
        if(medico == null){
            throw new DadosInvalidosException("O medico responsavél deve ser informado! ");
        }
        
        if(dataEmissao == null || validade == null){
            throw new DataInvalidaException("As datas de emissão e validade são obrigatórias.");
        }
        
        if(validade.isBefore(dataEmissao)){
            throw new DataInvalidaException("A data de validade ( " + validade.toString() +
            " ) não pode ser anterior à data de emissão ( " + dataEmissao.toString() + " )" );
        }
        
        this.medicamento = medicamento;
        this.quantidade = quantidade;
        this.paciente = paciente;
        this.medico = medico;
        this.dataEmissao = dataEmissao;
        this.validade = validade;
        
    }
    
    public String getMedicamento() { return medicamento; }
    public LocalDate getDataEmissao() { return dataEmissao; }
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
