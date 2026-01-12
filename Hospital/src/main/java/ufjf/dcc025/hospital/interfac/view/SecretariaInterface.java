package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.DefaultTableModel;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;

public class SecretariaInterface extends JFrame {

    private final Hospital hospital;
    private DefaultTableModel modelConsultas;
    private GerenciamentoDadosView painelGerenciamento; // Componente refatorado

    public SecretariaInterface(Recepcionista secretaria, Hospital hospital) {
        this.hospital = hospital;
        
        configurarJanela(secretaria.getNome());
        configurarMenu();

        JTabbedPane abas = new JTabbedPane();
        
        // Inicializa o painel de gerenciamento (Pacientes/Médicos)
        painelGerenciamento = new GerenciamentoDadosView(hospital);
        abas.addTab(" Gerenciamento", painelGerenciamento);
        
        // Inicializa o painel de agendamentos
        abas.addTab(" Agendamentos", criarPainelAgendamento());

        add(abas);
        setVisible(true);
    }

    private void configurarJanela(String nome) {
        setTitle("Sistema Hospitalar - Secretaria: " + nome);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Sessão");
        JMenuItem itemSair = new JMenuItem("Sair (Voltar ao Login)");
        itemSair.addActionListener(e -> voltarAoLogin());
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);
    }

    private JPanel criarPainelAgendamento() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        modelConsultas = new DefaultTableModel(new String[]{"Médico", "Data", "Hora", "Status", "Paciente"}, 0);
        JTable tabela = new JTable(modelConsultas);
        
        JButton btnAgendar = new JButton(" Marcar Nova Consulta");
        btnAgendar.setBackground(new Color(142, 68, 173));
        btnAgendar.setForeground(Color.WHITE);
        btnAgendar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAgendar.setPreferredSize(new Dimension(0, 45));

        btnAgendar.addActionListener(e -> {
            AgendaConsultasView view = new AgendaConsultasView(hospital);
            view.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) { atualizarTabelaConsultas(); }
            });
            view.setVisible(true);
        });

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnAgendar, BorderLayout.SOUTH);
        atualizarTabelaConsultas();
        return painel;
    }

    public void atualizarTabelaConsultas() {
        modelConsultas.setRowCount(0);
        for (Consulta c : hospital.getConsultasGerais()) {
            modelConsultas.addRow(new Object[]{
                c.getMedico().getNome(), 
                c.getData(), 
                c.getDataHoraInicio().toLocalTime(), 
                c.getStatus(), 
                (c.getPaciente() != null ? c.getPaciente().getNome() : "Livre")
            });
        }
    }

    private void voltarAoLogin() {
        if (JOptionPane.showConfirmDialog(this, "Deseja sair?", "Sair", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginInterface(hospital).setVisible(true);
        }
    }
}