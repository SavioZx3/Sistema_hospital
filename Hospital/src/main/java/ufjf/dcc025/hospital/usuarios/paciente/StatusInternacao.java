
package ufjf.dcc025.hospital.usuarios.paciente;

public enum StatusInternacao {
    
    INTERNADO("O paciente encontra-se ocupando um leito no hospital."),
    NAO_INTERNADO("O paciente não está ocupando leito.");
    
    private final String descricao;
    
    StatusInternacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
