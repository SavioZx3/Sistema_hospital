package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class VisitasMedicoView extends JPanel {

    private final Hospital hospital;
    private final Medico medico;

    private JTable tabela;
    private DefaultTableModel model;

    public VisitasMedicoView(Hospital hospital, Medico medico) {
        this.hospital = hospital;
        this.medico = medico;

        setLayout(new BorderLayout(10, 10));
        inicializarComponentes();
        atualizar();
    }

    // ======================= INICIALIZAÇÃO =======================

    private void inicializarComponentes() {
        criarTabela();
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void criarTabela() {
        model = new DefaultTableModel(
            new String[]{"Paciente", "CPF", "Status", "Ação"}, 0
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
    }

    // ======================= DADOS =======================

    public void atualizar() {
        popularTabela();
    }

    private void popularTabela() {
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

    // ======================= AÇÃO =======================

    private void abrirVisita(int linha) {
        String cpf = (String) model.getValueAt(linha, 1);
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);

        if (paciente == null) {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado.");
            return;
        }

        Window parent = SwingUtilities.getWindowAncestor(this);
        HistoricoPacienteView view =
                new HistoricoPacienteView(parent, paciente);

        view.setVisible(true);

        tratarAlta(paciente);
        atualizar();
    }

    private void tratarAlta(Paciente paciente) {
        Object[] opcoes = {"Manter Internado", "Dar Alta"};

        int escolha = JOptionPane.showOptionDialog(
                this,
                "Deseja realizar alguma ação para " + paciente.getNome() + "?",
                "Ações da Visita",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == 1) {
            paciente.setStatusInternacao(StatusInternacao.ALTA);
            JOptionPane.showMessageDialog(this, "Alta registrada com sucesso.");
        }
    }

    // ======================= BOTÃO (RENDERER) =======================

    private class BotaoRenderer extends JButton implements TableCellRenderer {

        public BotaoRenderer() {
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

    // ======================= BOTÃO (EDITOR) =======================

    private class BotaoEditor extends DefaultCellEditor {

        private final JButton botao;
        private int linhaAtual;

        public BotaoEditor(JCheckBox checkBox) {
            super(checkBox);
            botao = new JButton("Ver");

            botao.addActionListener(e -> {
                fireEditingStopped();
                abrirVisita(linhaAtual);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            linhaAtual = row;
            return botao;
        }
    }
}
