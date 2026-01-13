package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.servicos_hospital.consulta.*;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class AgendaMedicoView extends JPanel {

    private final Medico medico;
    private final VisitasMedicoView visitasView;

    private JTable tabela;
    private DefaultTableModel model;

    private static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("HH:mm");

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
                    "Abrir"
            });
        }
    }

    private void criarTabela() {
        model = new DefaultTableModel(
                new String[]{"Hora", "Paciente", "Status", "Ação"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) {
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

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            JOptionPane.showMessageDialog(this,
                    "Consulta já finalizada.");
            return;
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
    }

    private class BotaoRenderer extends JButton implements TableCellRenderer {
        public BotaoRenderer() { setText("Abrir"); }

        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean s, boolean f, int r, int c) {
            setEnabled(medico.getAgenda().getConsultas()
                    .get(r).getStatus() != StatusConsulta.REALIZADA);
            return this;
        }
    }

    private class BotaoEditor extends DefaultCellEditor {

        private int linha;

        public BotaoEditor(JCheckBox checkBox) {
            super(checkBox);
            JButton btn = new JButton("Abrir");
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
            return editorComponent;
        }
    }
}
