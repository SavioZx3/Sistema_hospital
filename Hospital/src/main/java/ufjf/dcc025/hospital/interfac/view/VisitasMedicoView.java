package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

/**
 * Tela de visitas médicas integrada ao Hospital.
 */
public class VisitasMedicoView extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private Hospital hospital;
    private Medico medicoLogado;

    public VisitasMedicoView(Hospital hospital, Medico medico) {
        this.hospital = hospital;
        this.medicoLogado = medico;
        setLayout(new BorderLayout());
        
        criarTabela();
        popularTabela();
    }

    private void criarTabela() {
        String[] colunas = {"Paciente", "CPF", "Status", "Ação"};

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Apenas a coluna 3 (botão Ver) é clicável
                return column == 3;
            }
        };

        tabela = new JTable(model);
        tabela.getColumn("Ação").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Ação").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Atualiza a tabela buscando apenas pacientes com status INTERNADO.
     */
    public void popularTabela() {
        model.setRowCount(0);
        List<Paciente> pacientes = hospital.listarPacientes();
        for (Paciente p : pacientes) {
            if (p.getStatusInternacao() == StatusInternacao.INTERNADO) {
                model.addRow(new Object[]{
                    p.getNome(),
                    p.getCpf(),
                    p.getStatusInternacao(),
                    "Ver"
                });
            }
        }
    }

    /**
     * Abre o diálogo de visita e evolução clínica.
     */
    private void abrirVisita(int linha) {
        String cpf = (String) model.getValueAt(linha, 1);
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);

        if (paciente == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Visita Médica", true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Área de texto para o médico ver dados e escrever evolução
        JTextArea prontuario = new JTextArea();
        prontuario.setBorder(BorderFactory.createTitledBorder("Evolução Clínica e Dados do Paciente"));
        
        // Montagem manual das informações já que getHistoricoCompleto() não existe
        String dadosPaciente = "NOME: " + paciente.getNome() + "\n" +
                               "CPF: " + paciente.getCpf() + "\n" +
                               "EMAIL: " + paciente.getEmail() + "\n" +
                               "------------------------------------------\n" +
                               "ANOTAÇÕES DA VISITA:\n";
        
        prontuario.setText(dadosPaciente);
        prontuario.setEditable(true);

        JPanel botoes = new JPanel();
        JButton btnAlta = new JButton("Dar Alta Médica");
        JButton btnFechar = new JButton("Fechar");

        // Ação de Alta
        btnAlta.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, 
                "Confirmar alta para " + paciente.getNome() + "?", "Confirmação de Alta", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Altera o status para NÃO_INTERNADO 
                paciente.setStatusInternacao(StatusInternacao.NAO_INTERNADO);
                JOptionPane.showMessageDialog(dialog, "Alta registrada com sucesso!");
                popularTabela(); // Recarrega a lista para remover o paciente que saiu
                dialog.dispose();
            }
        });

        btnFechar.addActionListener(e -> dialog.dispose());

        botoes.add(btnAlta);
        botoes.add(btnFechar);

        dialog.add(new JLabel(" Registro de Atendimento de Internação", SwingConstants.CENTER), BorderLayout.NORTH);
        dialog.add(new JScrollPane(prontuario), BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== CLASSES AUXILIARES PARA O BOTÃO NA TABELA =====

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setText("Ver"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Ver");
            button.addActionListener(e -> {
                fireEditingStopped();
                abrirVisita(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.currentRow = row;
            return button;
        }
    }
}
