package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;

import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

/**
 * Representa um atestado médico emitido a partir de uma consulta
 * O atestado só pode ser alterado enquanto não for emitido.
 */
public class Atestado {
    /** Texto do atestado */
    private String mensagem;

    /** Data de emissão do atestado */
    private LocalDate dataEmissao;

    /** Duração do afastamento (em dias) */
    private int duracaoDias;

    /** Paciente que receberá o atestado */
    private Paciente pacienteAlvo;

    /** Médico responsável pela emissão */
    private Medico medicoResponsavel;

    /** Indica se o atestado já foi emitido */
    private boolean emitido;
    
    
    public Atestado(Medico medico, Paciente paciente , String mensagem ,LocalDate dataEmissao ,int duracaoDias)
        throws DadosInvalidosException, DataInvalidaException{
        
        if(medico == null || paciente == null){
            throw new DadosInvalidosException("Paciente e Medico devem ser informados! ");
        }
            
        if(mensagem == null || mensagem.trim().isEmpty()){
            throw new DadosInvalidosException("A mensagem do atestado deve ser preenchida! ");
        }
        
        if(duracaoDias <= 0){
            throw new DadosInvalidosException("Deve ser informada uma duração válida para o atestado, no minimo 1 dia");
        }
        
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())){
            throw new DataInvalidaException("Data de emissão inválida ");
        }   
        
        this.medicoResponsavel = medico;
        this.pacienteAlvo = paciente;
        this.mensagem = mensagem;
        this.dataEmissao = dataEmissao;
        this.duracaoDias = duracaoDias;
        this.emitido = false;
    }

    //GETTERS QUE SERÃO/PODEM SER USADOS FORA DA CLASSE
    public String getMensagem(){
        return mensagem;
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

    //COMPORTAMENTOS DA CLASSE
    /**
     * Altera a mensagem do atestado apenas se ele ainda não tiver sido emitido.
     */
    public void alterarMensagem(String novaMensagem){
        if(emitido){
            throw new IllegalStateException("Atestado já emitido e não pode ser alterado.");
        }
        
        if(novaMensagem == null || novaMensagem.trim().isEmpty()){
            throw new DadosInvalidosException("Nova mensagem não deve ser vazia!");
        }
        
        this.mensagem = novaMensagem;
    }

     /**
     * Emite o atestado, impedindo alterações futuras.
     */
    public  void emitir(){
        emitido = true;
    }
}