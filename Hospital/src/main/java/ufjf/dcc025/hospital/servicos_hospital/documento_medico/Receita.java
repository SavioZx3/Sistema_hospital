package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Receita implements DocumentoMedico {

    private final LocalDate dataEmissao;
    private final Paciente paciente;
    private final Medico medico;
    
    public Receita(Paciente paciente , Medico medico , LocalDate dataEmissao) throws  DadosInvalidosException {    
        if(paciente == null ){
            throw new DadosInvalidosException("O paciente da receita deve ser informado! ");
        }
        
        if(medico == null){
            throw new DadosInvalidosException("O medico responsavél deve ser informado! ");
        }
        
        if(dataEmissao == null){
            throw new DataInvalidaException("A data de emissão é obrigatória.");
        }
        this.paciente = paciente;
        this.medico = medico;
        this.dataEmissao = dataEmissao;
    }
    
    @Override
    public Medico getMedico() { return medico; }

    @Override
    public Paciente getPaciente() { return paciente; }
 
    @Override
    public LocalDate getDataEmissao() { return dataEmissao; }
    
    @Override
    public String toString() {
    return """
           ==========================================
                        RECEITA MÉDICA               
           ==========================================
           MÉDICO: """ + medico.getNome() + "\n" +
           "PACIENTE: " + paciente.getNome() + "\n" +
           "------------------------------------------\n" +
           "MEDICAMENTO: Dipirona" +
           "QUANTIDADE: 100mg 2x ao dia" +
           "------------------------------------------\n" +
           "DATA DE EMISSÃO: " + dataEmissao + "\n" +
           "==========================================";
    }
}
