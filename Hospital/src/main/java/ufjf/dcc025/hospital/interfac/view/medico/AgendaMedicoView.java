package ufjf.dcc025.hospital.interfac.view.medico;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.interfac.view.BasePanel;
import ufjf.dcc025.hospital.servicos_hospital.consulta.*;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class AgendaMedicoView extends BasePanel {

    private final Medico medico;
    private final VisitasMedicoView visitasView;

    private JTable tabela;
    private DefaultTableModel model;

    private static final DateTimeFormatter HORA
            = DateTimeFormatter.ofPattern("HH:mm");

    public AgendaMedicoView(Medico medico, VisitasMedicoView visitasView) {
        this.medico = medico;
        this.visitasView = visitasView;

        setLayout(new BorderLayout(10, 10));
        criarTabela();
        atualizar();
    }

    public void atualizar() {
        model.setRowCount(0);

        for (Consulta c : medico.getAgenda().getConsultas()) {
            model.addRow(new Object[]{
                c.getDataHoraInicio().format(HORA),
                c.getPaciente() != null ? c.getPaciente().getNome() : "---",
                c.getStatus(),
                getTextoBotao(c)
            });
        }
    }

    private String getTextoBotao(Consulta c) {

        return switch (c.getStatus()) {
            case AGENDADA ->
                "Iniciar";
            case EM_ANDAMENTO ->
                "Finalizar";
            default ->
                "---";
        };
    }

    private void criarTabela() {
        model = new DefaultTableModel(
                new String[]{"Hora", "Paciente", "Status", "Ação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 3;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(28);

        tabela.getColumn("Ação").setCellRenderer(new BotaoRenderer());
        tabela.getColumn("Ação").setCellEditor(new BotaoEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void abrirConsulta(int linha) {
    Consulta consulta = medico.getAgenda().getConsultas().get(linha);

    try {
        switch (consulta.getStatus()) {

            case AGENDADA -> {
                consulta.marcarInicioConsulta();
            }

            case EM_ANDAMENTO -> {
                // segue normalmente
            }

            case REALIZADA -> {
                mostrarAviso("Consulta já finalizada.");
                return;
            }
        }

        new AtendimentoMedicoView(
                SwingUtilities.getWindowAncestor(this),
                medico,
                consulta,
                () -> {
                    atualizar();
                    visitasView.atualizar();
                }
        ).setVisible(true);

    } catch (Exception e) {
        mostrarErro(e.getMessage());
    }
}


    private class BotaoRenderer extends JButton implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean s, boolean f, int r, int c) {

            Consulta consulta = medico.getAgenda().getConsultas().get(r);

            setText(getTextoBotao(consulta));
            setEnabled(consulta.getStatus() != StatusConsulta.REALIZADA && consulta.getStatus() != StatusConsulta.DISPONIVEL);

            return this;
        }
    }

    private class BotaoEditor extends DefaultCellEditor {

        private int linha;
        private final JButton btn;

        public BotaoEditor(JCheckBox checkBox) {
            super(checkBox);
            btn = new JButton();

            btn.addActionListener(e -> {
                fireEditingStopped();
                abrirConsulta(linha);
            });

            editorComponent = btn;
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object v, boolean s, int r, int c) {

            linha = r;
            Consulta consulta = medico.getAgenda().getConsultas().get(r);

            btn.setText(getTextoBotao(consulta));
            btn.setEnabled(consulta.getStatus() != StatusConsulta.REALIZADA && consulta.getStatus() != StatusConsulta.DISPONIVEL);

            return btn;
        }
    }

}
