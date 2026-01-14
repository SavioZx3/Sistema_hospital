package ufjf.dcc025.hospital.interfac.controller;

/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class PacienteController {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Lista os exames buscando explicitamente do Histórico do paciente.
     */
    public Object[][] listarExames(Paciente p) {
        // Acessando via Historico para garantir consistência com a Persistência
        List<Exame> lista = p.getHistorico().getExames();
        Object[][] dados = new Object[lista.size()][3];

        for (int i = 0; i < lista.size(); i++) {
            Exame e = lista.get(i);
            dados[i][0] = e.getDataEmissao().format(dtf);
        }
        return dados;
    }

    /**
     * Lista as receitas buscando explicitamente do Histórico do paciente.
     */
    public Object[][] listarReceitas(Paciente p) {
        // Acessando via Historico
        List<Receita> lista = p.getHistorico().getReceitas();
        Object[][] dados = new Object[lista.size()][3];

        for (int i = 0; i < lista.size(); i++) {
            Receita r = lista.get(i);
            dados[i][0] = r.getDataEmissao().format(dtf);
        }
        return dados;
    }
}