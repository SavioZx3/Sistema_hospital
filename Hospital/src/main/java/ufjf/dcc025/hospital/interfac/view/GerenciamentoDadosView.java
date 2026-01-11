package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class GerenciamentoDadosView extends JPanel {

    private JTable tabela;
    private DefaultTableModel model;
    private Hospital hospital;
    private JComboBox<String> comboTipo;

    public GerenciamentoDadosView(Hospital hospital) {
        this.hospital = hospital;
        setLayout(new BorderLayout());

        criarPainelSuperior();
        criarTabela();
        popularTabela("Pacientes");
    }

    private void criarPainelSuperior() {
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Operações de Cadastro e Filtro"));

        painelBusca.add(new JLabel("Exibir:"));
        comboTipo = new JComboBox<>(new String[]{"Pacientes", "Médicos"});
        
        JButton btnCarregar = new JButton("Atualizar Lista");
        JButton btnNovoPaciente = new JButton("Novo Paciente");
        
        btnNovoPaciente.setBackground(new Color(46, 204, 113));
        btnNovoPaciente.setForeground(Color.WHITE);

        btnCarregar.addActionListener(e -> popularTabela((String) comboTipo.getSelectedItem()));
        
        btnNovoPaciente.addActionListener(e -> {
            // Abre o formulário de cadastro de paciente
            new CadastroPacienteView((Frame) SwingUtilities.getWindowAncestor(this), hospital);
            popularTabela("Pacientes"); // Atualiza após fechar
            comboTipo.setSelectedItem("Pacientes");
        });

        painelBusca.add(comboTipo);
        painelBusca.add(btnCarregar);
        painelBusca.add(new JSeparator(SwingConstants.VERTICAL));
        painelBusca.add(btnNovoPaciente);
        
        add(painelBusca, BorderLayout.NORTH);
    }

    private void criarTabela() {
        String[] colunas = {"Nome", "CPF", "Email", "Informação Adicional"};
        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(model);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    public void popularTabela(String tipo) {
        model.setRowCount(0);

        if (tipo.equals("Pacientes")) {
            List<Paciente> pacientes = hospital.listarPacientes();
            for (Paciente p : pacientes) {
                model.addRow(new Object[]{
                    p.getNome(), 
                    p.getCpf(), 
                    p.getEmail(), 
                    "Status: " + p.getStatusInternacao()
                });
            }
        } else {
            List<Medico> medicos = hospital.listarMedicos();
            for (Medico m : medicos) {
                model.addRow(new Object[]{
                    m.getNome(), 
                    m.getCpf(), 
                    m.getEmail(), 
                    "Espec: " + m.getEspecialidade()
                });
            }
        }
    }
}
