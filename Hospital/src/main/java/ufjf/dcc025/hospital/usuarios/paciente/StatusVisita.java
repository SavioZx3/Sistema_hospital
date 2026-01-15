package ufjf.dcc025.hospital.usuarios.paciente;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
public enum StatusVisita {
    
    APTO("O pasciente esta liberado para fazer/ receber visitas"),
    INAPTO("O pasciente não eta podendo fazer/receber visitas ");
    
    private final String descricao;
    
    StatusVisita(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
