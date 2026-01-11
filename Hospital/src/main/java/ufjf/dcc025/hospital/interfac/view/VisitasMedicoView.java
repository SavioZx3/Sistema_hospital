package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de visitas médicas.
 * Permite visualizar pacientes internados e finalizar visitas.
 */
public class VisitasMedicoView extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;

    public VisitasMedicoView() {
        setLayout(new BorderLayout());
        criarTabela();
    }

    private void criarTabela() {

        String[] colunas = {
            "Paciente",
            "Quarto",
            "Status",
            "Ação"
        };

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        // dados mock
        model.addRow(new Object[]{"João", "101", "Em visita", "Ver"});
        model.addRow(new Object[]{"Maria", "203", "Em visita", "Ver"});

        tabela = new JTable(model);
        tabela.getColumn("Ação").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Ação").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Abre a tela de detalhes da visita.
     */
    private void abrirVisita() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Visita Médica");
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea historico = new JTextArea();
        historico.setBorder(
                BorderFactory.createTitledBorder("Histórico do paciente")
        );

        historico.setText("""
                Histórico Clínico (mock)
                - Hipertensão
                - Consulta anterior em 10/01
                - Em observação
                """);

        JPanel botoes = new JPanel();
        JButton btnAlta = new JButton("Dar alta");
        JButton btnFechar = new JButton("Fechar");

        btnAlta.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Paciente recebeu alta com sucesso!",
                    "Alta médica",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose();
        });

        btnFechar.addActionListener(e -> dialog.dispose());

        botoes.add(btnAlta);
        botoes.add(btnFechar);

        dialog.add(
                new JLabel("Paciente: João | Quarto: 101",
                        SwingConstants.CENTER),
                BorderLayout.NORTH
        );
        dialog.add(new JScrollPane(historico), BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== CLASSES AUXILIARES =====

    private class ButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ButtonRenderer() {
            setText("Ver");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {

        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver");
            button.addActionListener(e -> abrirVisita());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {
            return button;
        }
    }
}