/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
package ufjf.dcc025.hospital.usuarios.paciente;

public enum StatusInternacao {
    
    INTERNADO("O paciente encontra-se ocupando um leito no hospital."),
    ALTA("O paciente estava internado mas foi liberado"),
    NAO_INTERNADO("O paciente não está ocupando leito.");
    
    private final String descricao;
    
    StatusInternacao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
