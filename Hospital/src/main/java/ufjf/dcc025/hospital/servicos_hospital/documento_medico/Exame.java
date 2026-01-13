package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
public class Exame implements DocumentoMedico {
    
    private final String tipoExame;
    private final String resultado;
    private final LocalDate dataEmissao;
    
    // transient impede que o GSON tente salvar o objeto inteiro recursivamente
    private transient Medico medico;
    private transient Paciente paciente;
    
    // Armazenamos o CPF para religar os objetos após carregar o JSON
    private String medicoCpf;
    private String pacienteCpf;

    public Exame(String tipoExame, LocalDate dataEmissao, Medico medico, Paciente paciente, String resultado) throws IllegalArgumentException {
        
        if(tipoExame == null || tipoExame.trim().isEmpty()) throw new IllegalArgumentException("Tipo de exame inválido");
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())) throw new IllegalArgumentException("Data inválida");
        if(medico == null || paciente == null) throw new IllegalArgumentException("Médico e Paciente são obrigatórios");
        if(resultado == null || resultado.trim().isEmpty()) throw new IllegalArgumentException("Resultado inválido");

        this.dataEmissao = dataEmissao;
        this.medico = medico;
        this.paciente = paciente;
        this.medicoCpf = medico.getCpf();
        this.pacienteCpf = paciente.getCpf();
        this.tipoExame = tipoExame;
        this.resultado = resultado;
    }
    
    public void reconstruir(List<Medico> medicos, List<Paciente> pacientes) {
        this.medico = medicos.stream().filter(m -> m.getCpf().equals(medicoCpf)).findFirst().orElse(null);
        this.paciente = pacientes.stream().filter(p -> p.getCpf().equals(pacienteCpf)).findFirst().orElse(null);
    }
    
    @Override public Medico getMedico() { return medico; }
    @Override public Paciente getPaciente() { return paciente; }
    @Override public LocalDate getDataEmissao() { return dataEmissao; }
    public String getTipoExame() { return tipoExame; }
    public String getResultado() { return resultado; }

    @Override
    public String toString() {
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