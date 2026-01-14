
package ufjf.dcc025.hospital.servicos_hospital.documento_medico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Exame implements DocumentoMedico {
    private final LocalDate dataEmissao;

    // transient impede que o GSON tente salvar o objeto inteiro recursivamente
    private transient Medico medico;
    private transient Paciente paciente;
    
    // Armazenamos o CPF para religar os objetos após carregar o JSON
    private String medicoCpf;
    private String pacienteCpf;

    public Exame(LocalDate dataEmissao, Medico medico, Paciente paciente) throws IllegalArgumentException {
        if(dataEmissao == null || dataEmissao.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Data de emissão inválida ");
        }

        if(medico == null)
            throw new IllegalArgumentException("Médico não informado");

        if(paciente == null)
            throw new IllegalArgumentException("Paciente não informado");
        
        this.dataEmissao = dataEmissao;
        this.medico = medico;
        this.paciente = paciente;
        this.medicoCpf = medico.getCpf();
        this.pacienteCpf = paciente.getCpf();
    }
    
    public void reconstruir(List<Medico> medicos, List<Paciente> pacientes) {
        this.medico = medicos.stream().filter(m -> m.getCpf().equals(medicoCpf)).findFirst().orElse(null);
        this.paciente = pacientes.stream().filter(p -> p.getCpf().equals(pacienteCpf)).findFirst().orElse(null);
    }

    @Override
    public Medico getMedico() { return medico; }

    @Override
    public Paciente getPaciente() { return paciente; }
 
    @Override
    public LocalDate getDataEmissao() { return dataEmissao; }
    @Override
    public String toString() {
        // Formata a data para o padrão brasileiro ao imprimir
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("""
            ========== LAUDO DE EXAME ==========
            PACIENTE: %s
            MÉDICO RESPONSAVEL: %s
            EXAME: SANGUE
            DATA: %s 
            ------------------------------------
            RESULTADO: PLAQUETAS: 250.000 /mm³
                       HEMOGLOBINA: 14 g/dL
            =====================================""", 
            paciente.getNome(), medico.getNome(), 
            dataEmissao.format(dtf));
    }
    
}
