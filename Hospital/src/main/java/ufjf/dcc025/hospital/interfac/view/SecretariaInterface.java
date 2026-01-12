package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class SecretariaInterface extends JFrame {

    private final Hospital hospital;
    private final Recepcionista recepcionista;
    private DefaultTableModel modelConsultas;
    private DefaultTableModel modelPacientes;

    public SecretariaInterface(Recepcionista secretaria, Hospital hospital) {
        this.hospital = hospital;
        this.recepcionista = secretaria;
        
        configurarJanela();
        configurarMenu();

        // Painel Superior Estilizado
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(44, 62, 80));
        painelSuperior.setPreferredSize(new Dimension(1100, 50));
        
        JLabel lblBemVindo = new JLabel("  SECRETARIA: " + recepcionista.getNome().toUpperCase());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JButton btnLogout = new JButton("Sair ⤶ ");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab(" Gestão de Cadastros", new GerenciamentoDadosView(hospital));
        abas.addTab(" Agenda de Consultas", criarPainelAgendamento());
        abas.addTab(" Pacientes e Internações", criarPainelBuscaPaciente());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Hospital - Painel Administrativo");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { executarSair(); }
        });
    }

    private void configurarMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Sistema");
        JMenuItem sair = new JMenuItem("Logout");
        sair.addActionListener(e -> executarSair());
        menu.add(sair);
        mb.add(menu);
        setJMenuBar(mb);
    }

    // ABA: Agendamentos com Atualização Automática
    private JPanel criarPainelAgendamento() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modelConsultas = new DefaultTableModel(new String[]{"Médico", "Data", "Hora", "Paciente", "Status"}, 0);
        JTable tabela = new JTable(modelConsultas);
        
        JButton btnAgendar = new JButton(" AGENDAR NOVA CONSULTA");
        btnAgendar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAgendar.setBackground(new Color(39, 174, 96));
        btnAgendar.setForeground(Color.WHITE);

        btnAgendar.addActionListener(e -> {
            AgendaConsultasView agendaView = new AgendaConsultasView(hospital);
            // Listener para atualizar a tabela assim que a tela de agendamento for fechada
            agendaView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    atualizarTabelaConsultas();
                }
            });
            agendaView.setVisible(true);
        });

        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        p.add(btnAgendar, BorderLayout.SOUTH);
        
        atualizarTabelaConsultas();
        return p;
    }

    // ABA: Busca e Histórico
    private JPanel criarPainelBuscaPaciente() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscaCPF = new JTextField(15);
        JButton btnBuscar = new JButton("🔍 Buscar por CPF");
        JButton btnLimpar = new JButton("Mostrar Internados");
        
        painelBusca.add(new JLabel("CPF:"));
        painelBusca.add(txtBuscaCPF);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnLimpar);

        modelPacientes = new DefaultTableModel(new String[]{"Nome", "CPF", "Status Internação", "Histórico"}, 0);
        JTable tabelaPacientes = new JTable(modelPacientes);

        btnBuscar.addActionListener(e -> {
            Paciente pac = hospital.buscarPacientePorCpf(txtBuscaCPF.getText().trim());
            if(pac != null) {
                modelPacientes.setRowCount(0);
                modelPacientes.addRow(new Object[]{
                    pac.getNome(), pac.getCpf(), pac.getStatusInternacao(), 
                    pac.getHistorico().getConsultas().size() + " atendimentos"
                });
            } else {
                JOptionPane.showMessageDialog(this, "Paciente não localizado.");
            }
        });

        btnLimpar.addActionListener(e -> atualizarListaPacientes());

        p.add(painelBusca, BorderLayout.NORTH);
        p.add(new JScrollPane(tabelaPacientes), BorderLayout.CENTER);
        
        atualizarListaPacientes();
        return p;
    }

    public void atualizarTabelaConsultas() {
        if (modelConsultas == null) return;
        modelConsultas.setRowCount(0);
        for (Consulta c : hospital.getConsultasGerais()) {
            modelConsultas.addRow(new Object[]{
                c.getMedico().getNome(), 
                c.getData(), 
                c.getDataHoraInicio().toLocalTime(), 
                (c.getPaciente() != null ? c.getPaciente().getNome() : "--- LIVRE ---"),
                c.getStatus()
            });
        }
    }

    private void atualizarListaPacientes() {
        if (modelPacientes == null) return;
        modelPacientes.setRowCount(0);
        for (Paciente p : hospital.listarPacientes()) {
            if (p.getStatusInternacao() == StatusInternacao.INTERNADO) {
                modelPacientes.addRow(new Object[]{
                    p.getNome(), p.getCpf(), "INTERNADO", p.getHistorico().getConsultas().size() + " registros"
                });
            }
        }
    }

    private void executarSair() {
        if (JOptionPane.showConfirmDialog(this, "Deseja encerrar a sessão?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }
}