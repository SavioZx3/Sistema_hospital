package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;

import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

/**
 * Representa um atestado médico emitido a partir de uma consulta
 * O atestado só pode ser alterado enquanto não for emitido.
 */
public class Atestado implements DocumentoMedico {


    private final LocalDate dataEmissao;

    /** Duração do afastamento (em dias) */
    private final int diasAfastamento;

 
    private  transient Paciente pacienteAlvo;
    private  transient Medico medicoResponsavel;
    
    private String pacienteCpf;
    private String medicoCpf;
        
    public Atestado(Medico medico, Paciente paciente ,LocalDate dataEmissao ,int diasAfastamento) throws DadosInvalidosException, DataInvalidaException{
        
        if(medico == null || paciente == null){
            throw new DadosInvalidosException("Paciente e Medico devem ser informados! ");
        }
        
        if(diasAfastamento <= 0){
            throw new DadosInvalidosException("Deve ser informada uma duração válida para o atestado, no minimo 1 dia");
        }
        
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())){
            throw new DataInvalidaException("Data de emissão inválida ");
        }   
        
        this.medicoResponsavel = medico;
        this.pacienteAlvo = paciente;
        this.dataEmissao = dataEmissao;
        this.diasAfastamento = diasAfastamento;
        this.medicoCpf = medico.getCpf(); // Salva o CPF
        this.pacienteCpf = paciente.getCpf(); // Salva o CPF
    }

    @Override
    public Medico getMedico() { return medicoResponsavel; }
    @Override
    public Paciente getPaciente() { return pacienteAlvo; }

    @Override
    public LocalDate getDataEmissao() { return dataEmissao; }

    public int getDiasAfastamento(){ return diasAfastamento; }
    
    public void reconstruir(List<Medico> medicos, List<Paciente> pacientes) {
        this.medicoResponsavel = medicos.stream().filter(m -> m.getCpf().equals(medicoCpf)).findFirst().orElse(null);
        this.pacienteAlvo = pacientes.stream().filter(p -> p.getCpf().equals(pacienteCpf)).findFirst().orElse(null);
    }


    @Override
    public String toString() {
        // Formata a data para o padrão brasileiro ao imprimir
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("""
            ========== ATESTADO MÉDICO ==========
            PACIENTE: %s
            MÉDICO RESPONSAVEL: %s
            DATA DE EMISSÃO: %s 
            DURAÇÃO (DIAS): %d
            ------------------------------------
            O PACIENTE ACIMA FOI AFASTADO DE SUAS ATIVIDADES PELO PERÍODO INDICADO.
            =====================================""", 
            pacienteAlvo.getNome(), medicoResponsavel.getNome(), dataEmissao.format(dtf), diasAfastamento);
    }
}