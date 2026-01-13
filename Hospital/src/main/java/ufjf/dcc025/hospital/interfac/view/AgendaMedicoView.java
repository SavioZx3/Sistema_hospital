package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class AgendaMedicoView extends JPanel {

    private final Medico medico;

    private JTable tabela;
    private DefaultTableModel model;

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    public AgendaMedicoView(Medico medico) {
        this.medico = medico;
        setLayout(new BorderLayout(10, 10));
        criarTabela();
        atualizar();
    }

    // ======================= ATUALIZAÇÃO =======================

    public void atualizar() {
        popularTabela();
    }

    private void popularTabela() {
        model.setRowCount(0);

        for (Consulta consulta : medico.getAgenda().getConsultas()) {
            model.addRow(new Object[]{
                    consulta.getDataHoraInicio().format(FORMATO_HORA),
                    consulta.getPaciente() != null
                            ? consulta.getPaciente().getNome()
                            : "---",
                    consulta.getStatus(),
                    "Abrir"
            });
        }
    }

    // ======================= TABELA =======================

    private void criarTabela() {
        model = new DefaultTableModel(
                new String[]{"Hora", "Paciente", "Status", "Ação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(28);

        tabela.getColumn("Ação").setCellRenderer(new BotaoRenderer());
        tabela.getColumn("Ação").setCellEditor(new BotaoEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    // ======================= AÇÃO =======================

    private void abrirConsulta(int linha) {
        Consulta consulta = medico.getAgenda().getConsultas().get(linha);

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            JOptionPane.showMessageDialog(this,
                    "Esta consulta já foi finalizada.");
            return;
        }

        AtendimentoMedicoView view = new AtendimentoMedicoView(
                SwingUtilities.getWindowAncestor(this),
                medico,
                consulta
        );

        view.setVisible(true);
        atualizar();
    }

    // ======================= BOTÃO =======================

    private class BotaoRenderer extends JButton
            implements TableCellRenderer {

        public BotaoRenderer() {
            setText("Abrir");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Consulta c = medico.getAgenda().getConsultas().get(row);
            setEnabled(c.getStatus() != StatusConsulta.REALIZADA);
            return this;
        }
    }

    private class BotaoEditor extends DefaultCellEditor {

        private final JButton botao;
        private int linhaAtual;

        public BotaoEditor(JCheckBox checkBox) {
            super(checkBox);
            botao = new JButton("Abrir");

            botao.addActionListener(e -> {
                fireEditingStopped();
                abrirConsulta(linhaAtual);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            linhaAtual = row;
            Consulta c = medico.getAgenda().getConsultas().get(row);
            botao.setEnabled(c.getStatus() != StatusConsulta.REALIZADA);
            return botao;
        }
    }
}
