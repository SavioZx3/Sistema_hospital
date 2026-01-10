package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de consultas do médico.
 * Exibe as consultas agendadas e permite abrir os detalhes.
 */
public class ConsultasMedicoView extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;

    public ConsultasMedicoView() {
        setLayout(new BorderLayout());
        criarTabela();
    }

    private void criarTabela() {

        String[] colunas = {
            "Paciente",
            "Dia",
            "Horário",
            "Ação"
        };

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // só o botão
            }
        };

        // dados mock
        model.addRow(new Object[]{"João", "Segunda", "09:00", "Ver consulta"});
        model.addRow(new Object[]{"Maria", "Terça", "10:30", "Ver consulta"});

        tabela = new JTable(model);
        tabela.getColumn("Ação").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Ação").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Abre a janela de detalhes da consulta.
     */
    private void abrirConsulta() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Consulta");
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setBorder(BorderFactory.createTitledBorder("Registro médico"));

        JPanel botoes = new JPanel();
        botoes.add(new JButton("Atestado"));
        botoes.add(new JButton("Receita"));
        botoes.add(new JButton("Exame"));

        dialog.add(new JLabel("Paciente: João | Dia: Segunda | 09:00",
                SwingConstants.CENTER), BorderLayout.NORTH);
        dialog.add(new JScrollPane(area), BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== CLASSES AUXILIARES =====

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver consulta");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver consulta");
            button.addActionListener(e -> abrirConsulta());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {
            return button;
        }
    }
}