package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;

import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

/**
 * Representa um atestado médico emitido a partir de uma consulta
 * O atestado só pode ser alterado enquanto não for emitido.
 */
public class Atestado {


    private LocalDate dataEmissao;

    /** Duração do afastamento (em dias) */
    private int duracaoDias;

 
    private Paciente pacienteAlvo;


    private Medico medicoResponsavel;
        
    public Atestado(Medico medico, Paciente paciente ,LocalDate dataEmissao ,int duracaoDias) throws DadosInvalidosException, DataInvalidaException{
        
        if(medico == null || paciente == null){
            throw new DadosInvalidosException("Paciente e Medico devem ser informados! ");
        }
        
        if(duracaoDias <= 0){
            throw new DadosInvalidosException("Deve ser informada uma duração válida para o atestado, no minimo 1 dia");
        }
        
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())){
            throw new DataInvalidaException("Data de emissão inválida ");
        }   
        
        this.medicoResponsavel = medico;
        this.pacienteAlvo = paciente;
        this.dataEmissao = dataEmissao;
        this.duracaoDias = duracaoDias;
    }


    public LocalDate getDataEmissao(){
        return dataEmissao;
    }

    public int getDuracaoDias(){
        return duracaoDias;
    }

    public Paciente getPacienteAlvo(){
        return pacienteAlvo;
    }

    public Medico getMedicoResponsavel(){
        return medicoResponsavel;
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
            pacienteAlvo.getNome(), medicoResponsavel.getNome(), dataEmissao.format(dtf), duracaoDias);
    }
}