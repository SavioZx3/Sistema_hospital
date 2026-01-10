package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Exame implements DocumentoMedico {
    
    private final String tipoExame;
    private final String resultado;
    private final LocalDate dataEmissao;
    private final Medico medico;
    private final Paciente paciente;

    public Exame(String tipoExame, LocalDate dataEmissao, Medico medico, Paciente paciente, String resultado) throws IllegalArgumentException {
        if(tipoExame == null || tipoExame.trim().isEmpty()){
            throw new IllegalArgumentException("O tipo de exame deve ser informado! ");
        }
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Data de emissão inválida ");
        }

        if(medico == null)
            throw new IllegalArgumentException("Médico não informado");

        if(paciente == null)
            throw new IllegalArgumentException("Paciente não informado");

        if(resultado == null || resultado.trim().isEmpty())
            throw new IllegalArgumentException("Resultado do exame deve ser informado");


        
        this.dataEmissao = dataEmissao;
        this.medico = medico;
        this.paciente = paciente;
        this.tipoExame = tipoExame;
        this.resultado = resultado;
    }
    
    @Override
    public Medico getMedico() { return medico; }

    @Override
    public Paciente getPaciente() { return paciente; }
 
    @Override
    public LocalDate getDataEmissao() { return dataEmissao; }

    public String getTipoExame() { return tipoExame; }
    public String getResultado() { return resultado; }
  

    
    
    @Override
    public String toString() {
        // Formata a data para o padrão brasileiro ao imprimir
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("""
            ========== LAUDO DE EXAME ==========
            PACIENTE: %s
            MÉDICO RESPONSAVEL: %s
            EXAME: %s
            DATA: %s 
            ------------------------------------
            RESULTADO: %s
            =====================================""", 
            paciente.getNome(), medico.getNome(), tipoExame, 
            dataEmissao.format(dtf), resultado);
    }
    
}
