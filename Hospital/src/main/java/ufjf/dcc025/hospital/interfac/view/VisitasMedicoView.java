package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

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
                return column == 3;
            }
        };

        tabela = new JTable(model);
        tabela.getColumn("Ação").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Ação").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

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

    private void abrirVisita(int linha) {
        // 1. Pegamos o CPF da linha selecionada
        String cpf = (String) model.getValueAt(linha, 1);
        
        // 2. Buscamos o objeto Paciente real no hospital
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);

        if (paciente != null) {
            // 3. Abrimos a tela de histórico que criamos
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            HistoricoPacienteView hView = new HistoricoPacienteView(parentWindow, paciente);
            hView.setVisible(true);
            
            // Caso o médico queira dar alta após ver o histórico:
            Object[] opcoes = {"Manter Internado", "Dar Alta"};
            int escolha = JOptionPane.showOptionDialog(this, 
                    "Deseja realizar alguma ação para " + paciente.getNome() + "?",
                    "Ações de Visita",
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, opcoes, opcoes[0]);
            
            if (escolha == 1) { // Dar Alta
                paciente.setStatusInternacao(StatusInternacao.ALTA);
                JOptionPane.showMessageDialog(this, "Alta registrada.");
                popularTabela(); // Atualiza a lista
            }
        }
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