package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela da agenda do médico.
 * Permite configurar dias de atendimento e horários.
 */
public class AgendaMedicoView extends JPanel {

    private JTable tabelaAgenda;
    private DefaultTableModel model;

    public AgendaMedicoView() {
        setLayout(new BorderLayout());
        criarTabela();
        criarRodape();
    }

    /**
     * Cria a tabela da agenda.
     */
    private void criarTabela() {

        String[] colunas = {
            "Atende",
            "Dia da semana",
            "Horário início",
            "Horário término",
            "Duração"
        };

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Boolean.class; // checkbox
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        adicionarDiasSemana();

        tabelaAgenda = new JTable(model);

        configurarCombos();

        JScrollPane scrollPane = new JScrollPane(tabelaAgenda);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adiciona os dias da semana na tabela.
     */
    private void adicionarDiasSemana() {
        String[] dias = {
            "Segunda",
            "Terça",
            "Quarta",
            "Quinta",
            "Sexta"
        };

        for (String dia : dias) {
            model.addRow(new Object[]{
                false,
                dia,
                "08:00",
                "12:00",
                "30 min"
            });
        }
    }

    /**
     * Configura os JComboBox nas colunas.
     */
    private void configurarCombos() {

        String[] horarios = {
            "07:00", "08:00", "09:00", "10:00",
            "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00"
        };

        String[] duracoes = {
            "20 min",
            "30 min",
            "40 min",
            "60 min"
        };

        JComboBox<String> comboInicio = new JComboBox<>(horarios);
        JComboBox<String> comboTermino = new JComboBox<>(horarios);
        JComboBox<String> comboDuracao = new JComboBox<>(duracoes);

        tabelaAgenda.getColumnModel().getColumn(2)
                .setCellEditor(new DefaultCellEditor(comboInicio));

        tabelaAgenda.getColumnModel().getColumn(3)
                .setCellEditor(new DefaultCellEditor(comboTermino));

        tabelaAgenda.getColumnModel().getColumn(4)
                .setCellEditor(new DefaultCellEditor(comboDuracao));
    }

    /**
     * Rodapé com botão de salvar.
     */
    private void criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSalvar = new JButton("Salvar agenda");

        btnSalvar.addActionListener(e -> salvarAgenda());

        rodape.add(btnSalvar);
        add(rodape, BorderLayout.SOUTH);
    }

    /**
     * Mock de salvamento da agenda.
     * Depois será ligado ao controller/model.
     */
    private void salvarAgenda() {
        JOptionPane.showMessageDialog(
            this,
            "Agenda salva com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}