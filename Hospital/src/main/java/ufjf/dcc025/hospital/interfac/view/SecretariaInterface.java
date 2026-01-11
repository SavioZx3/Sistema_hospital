package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;

public class SecretariaInterface extends JFrame {

    private final Hospital hospital;
    private DefaultTableModel modelConsultas;

    public SecretariaInterface(Recepcionista secretaria, Hospital hospital) {
        this.hospital = hospital;
        
        setTitle("Hospital - Secretaria: " + secretaria.getNome());
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Sessão");
        JMenuItem itemSair = new JMenuItem("Sair (Voltar ao Login)");
        itemSair.addActionListener(e -> voltarAoLogin());
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Gerenciamento", criarPainelGerenciamento());
        abas.addTab("Agendamentos", criarPainelAgendamento());

        add(abas);
    }

    private JPanel criarPainelGerenciamento() {
        JPanel painel = new JPanel(new BorderLayout());
        GerenciamentoDadosView viewDados = new GerenciamentoDadosView(hospital);
        painel.add(viewDados, BorderLayout.CENTER);

        JPanel pnlBotoesCad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBotoesCad.setBorder(BorderFactory.createTitledBorder("Ações de Cadastro"));

        JButton btnNovoMedico = new JButton("️ Novo Médico");
        btnNovoMedico.addActionListener(e -> new CadastroMedicoInterface(hospital).setVisible(true));

        JButton btnNovoPaciente = new JButton(" Novo Paciente");
        btnNovoPaciente.addActionListener(e -> new FormularioCadastroPacienteView(hospital).setVisible(true));

        pnlBotoesCad.add(btnNovoMedico);
        pnlBotoesCad.add(btnNovoPaciente);
        painel.add(pnlBotoesCad, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelAgendamento() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelConsultas = new DefaultTableModel(new String[]{"Médico", "Data", "Hora", "Status", "Paciente"}, 0);
        JTable tabela = new JTable(modelConsultas);

        JButton btnAgendar = new JButton(" Novo Agendamento");
        btnAgendar.setPreferredSize(new Dimension(250, 40));
        
        btnAgendar.addActionListener(e -> {
            AgendaConsultasView view = new AgendaConsultasView(hospital);
            view.recarregarPacientes(); // GARANTE DADOS ATUALIZADOS
            view.addWindowListener(new WindowAdapter() {
                @Override 
                public void windowClosed(WindowEvent e) { 
                    atualizarTabela(); // Atualiza a grid da secretaria ao fechar
                }
            });
            view.setVisible(true);
        });

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnAgendar, BorderLayout.SOUTH);
        
        atualizarTabela();
        return painel;
    }

    public void atualizarTabela() {
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
        if (JOptionPane.showConfirmDialog(this, "Deseja sair?", "Logout", JOptionPane.YES_NO_OPTION) == 0) {
            this.dispose();
            new LoginInterface(hospital).setVisible(true);
        }
    }
}