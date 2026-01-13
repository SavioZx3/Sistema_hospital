package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class GerenciamentoDadosView extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private Hospital hospital;
    private JComboBox<String> comboTipo;

    public GerenciamentoDadosView(Hospital hospital) {
        this.hospital = hospital;
        setLayout(new BorderLayout(10, 10));
        
        // Painel de Ações
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAcoes.setBorder(BorderFactory.createTitledBorder("Controle de Dados"));

        comboTipo = new JComboBox<>(new String[]{"Pacientes", "Médicos"});
        JButton btnAtualizar = new JButton(" Atualizar");
        JButton btnNovoPac = new JButton(" Novo Paciente");
        JButton btnNovoMed = new JButton("️ Novo Médico");
        JButton btnInternarAlta = new JButton(" Internar/Alta");

        // Estilização básica
        btnNovoPac.setBackground(new Color(41, 128, 185));
        btnNovoPac.setForeground(Color.WHITE);
        btnInternarAlta.setBackground(new Color(39, 174, 96));
        btnInternarAlta.setForeground(Color.WHITE);

        btnAtualizar.addActionListener(e -> popularTabela((String) comboTipo.getSelectedItem()));
        
        btnNovoPac.addActionListener(e -> {
            new CadastroPacienteView(hospital, () -> popularTabela("Pacientes")).setVisible(true);
        });

        btnNovoMed.addActionListener(e -> {
            new CadastroMedicoInterface(hospital, () -> popularTabela("Médicos")).setVisible(true);
        });

        btnInternarAlta.addActionListener(e -> gerenciarInternacao());

        pnlAcoes.add(new JLabel("Exibir:"));
        pnlAcoes.add(comboTipo);
        pnlAcoes.add(btnAtualizar);
        pnlAcoes.add(new JSeparator(SwingConstants.VERTICAL));
        pnlAcoes.add(btnNovoPac);
        pnlAcoes.add(btnNovoMed);
        pnlAcoes.add(btnInternarAlta);

        // Tabela
        model = new DefaultTableModel(new String[]{"Tipo", "Nome", "CPF", "Status/Espec."}, 0);
        tabela = new JTable(model);
        
        add(pnlAcoes, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        popularTabela("Pacientes");
    }

    private void gerenciarInternacao() {
        String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do paciente para Gerenciar Status:");
        if (cpf == null || cpf.isEmpty()) return;

        try {
            Paciente p = hospital.buscarPacientePorCpf(cpf);
            Object[] options = {"Internar", "Dar Alta", "Cancelar"};
            int res = JOptionPane.showOptionDialog(this, 
                "Paciente: " + p.getNome() + "\nStatus Atual: " + p.getStatusInternacao(),
                "Gerenciar Internação", JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (res == 0) p.setStatusInternacao(StatusInternacao.INTERNADO);
            else if (res == 1) p.setStatusInternacao(StatusInternacao.ALTA);

            popularTabela("Pacientes");
            comboTipo.setSelectedItem("Pacientes");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public void popularTabela(String tipo) {
        model.setRowCount(0);
        if (tipo.equals("Pacientes")) {
            for (Paciente p : hospital.listarPacientes()) {
                model.addRow(new Object[]{"PACIENTE", p.getNome(), p.getCpf(), p.getStatusInternacao()});
            }
        } else {
            for (Medico m : hospital.listarMedicos()) {
                model.addRow(new Object[]{"MÉDICO", m.getNome(), m.getCpf(), m.getEspecialidade()});
            }
        }
    }
}