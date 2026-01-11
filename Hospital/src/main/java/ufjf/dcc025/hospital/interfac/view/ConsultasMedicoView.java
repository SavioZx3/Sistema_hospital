package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;

public class ConsultasMedicoView extends JFrame {

    public ConsultasMedicoView(Medico medico) {

        setTitle("Consultas do Médico");
        setSize(700, 400);
        setLocationRelativeTo(null);

        String[] colunas = {"Data", "Início", "Fim", "Paciente", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (Consulta c : medico.getAgenda().getConsultas()) {
            model.addRow(new Object[]{
                c.getDataHoraInicio().toLocalDate(),
                c.getDataHoraInicio().toLocalTime(),
                c.getDataHoraFim().toLocalTime(),
                c.getPaciente() != null ? c.getPaciente().getNome() : "",
                c.getStatus()
            });
        }

        JTable tabela = new JTable(model);
        add(new JScrollPane(tabela));
        setVisible(true);
    }
}