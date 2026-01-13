package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class ConsultasMedicoView extends JFrame {

    private JTable tabela;
    private DefaultTableModel model;
    private Medico medicoLogado;
    private Hospital hospital;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ConsultasMedicoView(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;

        setTitle("Agenda de Consultas - Dr(a). " + medicoLogado.getNome());
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarTabela();
        popularTabela();

        setVisible(true);
    }

    private void criarTabela() {
        String[] colunas = {"Paciente", "Dia", "Horário", "Status", "Ação"};

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; 
            }
        };

        tabela = new JTable(model);
        tabela.getColumn("Ação").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Ação").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void popularTabela() {
        model.setRowCount(0);
        // Lista as consultas do médico que possuem paciente (agendadas ou concluídas)
        for (Consulta c : medicoLogado.getAgenda().getConsultas()) {
            if (c.getStatus() != StatusConsulta.DISPONIVEL) {
                String nomePaciente = (c.getPaciente() != null) ? c.getPaciente().getNome() : "---";
                model.addRow(new Object[]{
                    nomePaciente,
                    c.getData().toString(),
                    c.getDataHoraInicio().format(timeFormatter),
                    c.getStatus().toString(),
                    "Atender"
                });
            }
        }
    }

    private void abrirConsulta(int linha) {
        // Busca a consulta correspondente na agenda
        Consulta consultaSelecionada = null;
        int count = 0;
        for(Consulta c : medicoLogado.getAgenda().getConsultas()){
            if(c.getStatus() != StatusConsulta.DISPONIVEL){
                if(count == linha){
                    consultaSelecionada = c;
                    break;
                }
                count++;
            }
        }

        if (consultaSelecionada == null) return;

        final Consulta consultaFinal = consultaSelecionada;
        JDialog dialog = new JDialog(this, "Prontuário: " + consultaFinal.getPaciente().getNome(), true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea areaProntuario = new JTextArea();
        areaProntuario.setBorder(BorderFactory.createTitledBorder("Observações do Atendimento"));

        JPanel painelAcoes = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton btnAtestado = new JButton("Atestado");
        JButton btnReceita = new JButton("Receita");
        JButton btnConcluir = new JButton("Finalizar");

        // --- AÇÃO ATESTADO ---
        btnAtestado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dias = JOptionPane.showInputDialog("Quantidade de dias:");
                if(dias != null && !dias.isEmpty()) {
                    consultaFinal.getAtestado(Integer.parseInt(dias));
                    JOptionPane.showMessageDialog(dialog, "Atestado gerado!");
                }
            }
        });

        // --- AÇÃO RECEITA ---
        btnReceita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remedio = JOptionPane.showInputDialog("Medicamento:");
                if(remedio != null) {
                    consultaFinal.getReceita(remedio, "1x ao dia", LocalDate.now().plusDays(30));
                    JOptionPane.showMessageDialog(dialog, "Receita adicionada à consulta!");
                }
            }
        });

        // --- AÇÃO CONCLUIR ---
        btnConcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método do médico para processar o atendimento físico
                medicoLogado.realizarAtendimento(consultaFinal, 0, "N/A", "N/A", LocalDate.now());
                JOptionPane.showMessageDialog(dialog, "Atendimento finalizado com sucesso!");
                dialog.dispose();
                popularTabela(); // Atualiza a lista principal
            }
        });

        painelAcoes.add(btnAtestado);
        painelAcoes.add(btnReceita);
        painelAcoes.add(btnConcluir);

        dialog.add(new JLabel(" Atendimento em curso...", SwingConstants.CENTER), BorderLayout.NORTH);
        dialog.add(new JScrollPane(areaProntuario), BorderLayout.CENTER);
        dialog.add(painelAcoes, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== RENDERERS E EDITORS =====

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setText("Atender"); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Atender");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    abrirConsulta(row);
                }
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }
}
