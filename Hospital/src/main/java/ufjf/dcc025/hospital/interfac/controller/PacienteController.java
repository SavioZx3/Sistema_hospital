package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class PacienteController {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Lista os exames reais do paciente para a JTable.
     */
    public Object[][] listarExames(Paciente p) {
        List<Exame> lista = p.getExames();
        Object[][] dados = new Object[lista.size()][3];

        for (int i = 0; i < lista.size(); i++) {
            Exame e = lista.get(i);
            dados[i][0] = e.getTipoExame();
            dados[i][1] = e.getDataEmissao().format(dtf);
            dados[i][2] = e.getResultado();
        }
        return dados;
    }

    /**
     * Lista as receitas reais do paciente para a JTable.
     */
    public Object[][] listarReceitas(Paciente p) {
        List<Receita> lista = p.getReceitas();
        Object[][] dados = new Object[lista.size()][3];

        for (int i = 0; i < lista.size(); i++) {
            Receita r = lista.get(i);
            dados[i][0] = r.getMedicamento();
            dados[i][1] = r.getDataEmissao().format(dtf);
            dados[i][2] = r.getValidade().format(dtf);
        }
        return dados;
    }
}