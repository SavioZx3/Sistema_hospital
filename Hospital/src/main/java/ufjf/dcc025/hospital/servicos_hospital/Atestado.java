package ufjf.dcc025.hospital.servicos_hospital;

import java.time.LocalDate;

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
    
    
    public Atestado(Medico medico, Paciente paciente , String mensagem ,LocalDate dataEmissao ,int duracaoDias){
            
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
        if(!emitido)
            mensagem = novaMensagem;
    }

     /**
     * Emite o atestado, impedindo alterações futuras.
     */
    public  void emitir(){
        emitido = true;
    }
}