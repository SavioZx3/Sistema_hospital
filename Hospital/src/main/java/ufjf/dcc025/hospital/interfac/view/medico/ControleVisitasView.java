package ufjf.dcc025.hospital.interfac.view.medico;

/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusVisita;

public class ControleVisitasView extends JFrame {

    private JTable tabela;
    private DefaultTableModel model;
    private Hospital hospital;

    public ControleVisitasView(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Controle de Visitas - Hospital");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarTabela();
        criarPainelAcoes();

        setVisible(true);
    }

    private void criarTabela() {
        String[] colunas = {"Paciente", "CPF", "Status Visita", "Internação"};

        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        atualizarDados();

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void atualizarDados() {
        model.setRowCount(0);

        List<Paciente> pacientes = hospital.listarPacientes();

        for (Paciente p : pacientes) {
            model.addRow(new Object[]{
                p.getNome(),
                p.getCpf(),
                p.getStatusVisita(),
                p.getStatusInternacao()
            });
        }
    }

    private void criarPainelAcoes() {
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAlternar = new JButton("Alternar Permissão de Visita");
        JButton btnAtualizar = new JButton(" Atualizar");

        btnAlternar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    // Pegamos o CPF da coluna 1 para buscar o paciente único
                    String cpf = (String) model.getValueAt(linha, 1);

                    try {
                        Paciente p = hospital.buscarPacientePorCpf(cpf);

                        // Lógica de alternância
                        p.setStatusVisita(p.getStatusVisita() == StatusVisita.APTO ? StatusVisita.INAPTO : StatusVisita.APTO);
                        hospital.salvarAlteracoes(); //Salva o JSON
                        atualizarDados();

                        atualizarDados();
                        JOptionPane.showMessageDialog(null, "Status de visitas de " + p.getNome() + " alterado!");

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao buscar paciente: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um paciente na tabela.");
                }
            }
        });

        btnAtualizar.addActionListener(e -> atualizarDados());

        painelAcoes.add(btnAtualizar);
        painelAcoes.add(btnAlternar);
        add(painelAcoes, BorderLayout.SOUTH);
    }
}
