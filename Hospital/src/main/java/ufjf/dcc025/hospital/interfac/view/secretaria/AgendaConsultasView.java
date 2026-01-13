package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class AgendaConsultasView extends JFrame {
    private final Hospital hospital;
    private Paciente pacienteSelecionado;
    
    private DefaultTableModel model;
    private JTable tabela;
    private JTextField txtCpf;
    private JComboBox<Paciente> comboPacientes; 
    private JLabel lblStatusSelecao;

    public AgendaConsultasView(Hospital hospital) {
        this.hospital = hospital;
        setTitle("Realizar Agendamento");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- PAINEL SUPERIOR: SELEÇÃO DE PACIENTE ---
        JPanel pnlTopo = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlTopo.setBorder(BorderFactory.createTitledBorder("1. Selecione o Paciente"));

        // Linha 1: Seleção por Lista
        JPanel pnlLista = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLista.add(new JLabel("Pacientes Cadastrados:"));
        
        // Inicializa o combo vazio e carrega os dados
        comboPacientes = new JComboBox<>();
        recarregarPacientes(); 
        
        comboPacientes.addActionListener(e -> {
            if (comboPacientes.getSelectedItem() != null) {
                pacienteSelecionado = (Paciente) comboPacientes.getSelectedItem();
                txtCpf.setText(pacienteSelecionado.getCpf());
                lblStatusSelecao.setText("Selecionado: " + pacienteSelecionado.getNome());
                lblStatusSelecao.setForeground(new Color(0, 100, 0)); // Verde se ok
            }
        });
        
        pnlLista.add(comboPacientes);

        // Linha 2: Busca por CPF
        JPanel pnlCpf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCpf.add(new JLabel("Ou busque por CPF:"));
        txtCpf = new JTextField(12);
        JButton btnBuscar = new JButton(" Buscar");
        lblStatusSelecao = new JLabel("Nenhum paciente selecionado");
        lblStatusSelecao.setForeground(Color.RED);
        lblStatusSelecao.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnBuscar.addActionListener(e -> buscarPorCpf());
        
        pnlCpf.add(txtCpf);
        pnlCpf.add(btnBuscar);
        pnlCpf.add(lblStatusSelecao);

        pnlTopo.add(pnlLista);
        pnlTopo.add(pnlCpf);

        // --- PAINEL CENTRAL: TABELA DE HORÁRIOS ---
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBorder(BorderFactory.createTitledBorder("2. Escolha o Horário Disponível"));

        model = new DefaultTableModel(new String[]{"Médico", "Data", "Início", "Término", "Objeto"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(model);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4)); 
        
        atualizarTabelaHorarios();
        pnlCentro.add(new JScrollPane(tabela), BorderLayout.CENTER);

        // --- PAINEL INFERIOR: AÇÕES ---
        JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton("Confirmar Agendamento");
        btnConfirmar.setPreferredSize(new Dimension(200, 40));
        btnConfirmar.setBackground(new Color(46, 204, 113));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 13));

        btnConfirmar.addActionListener(e -> realizarAgendamento());
        pnlSul.add(btnConfirmar);

        add(pnlTopo, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
        add(pnlSul, BorderLayout.SOUTH);
    }

    private void buscarPorCpf() {
        try {
            String cpf = txtCpf.getText().trim();
            pacienteSelecionado = hospital.buscarPacientePorCpf(cpf);
            lblStatusSelecao.setText("Selecionado: " + pacienteSelecionado.getNome());
            lblStatusSelecao.setForeground(new Color(0, 100, 0));
            comboPacientes.setSelectedItem(pacienteSelecionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Paciente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabelaHorarios() {
        model.setRowCount(0);
        for (Consulta c : hospital.getConsultasGerais()) {
            if (c.getStatus() == StatusConsulta.DISPONIVEL) {
                model.addRow(new Object[]{
                    c.getMedico().getNome(),
                    c.getData(),
                    c.getDataHoraInicio().toLocalTime(),
                    c.getDataHoraFim().toLocalTime(),
                    c
                });
            }
        }
    }

    private void realizarAgendamento() {
        int linha = tabela.getSelectedRow();
        if (pacienteSelecionado == null || linha == -1) {
            JOptionPane.showMessageDialog(this, "Atenção: Selecione o PACIENTE e o HORÁRIO.");
            return;
        }

        Consulta consulta = (Consulta) model.getValueAt(linha, 4);
        try {
            hospital.agendarConsultaNaAgenda(pacienteSelecionado, consulta.getMedico(), consulta);
            JOptionPane.showMessageDialog(this, "Sucesso! Consulta marcada para " + pacienteSelecionado.getNome());
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
    
    public final void recarregarPacientes() {
        comboPacientes.removeAllItems();
        comboPacientes.addItem(null); // Opção nula padrão
        List<Paciente> pacientes = hospital.listarPacientes();
        for (Paciente p : pacientes) {
            comboPacientes.addItem(p);
        }
    }
}