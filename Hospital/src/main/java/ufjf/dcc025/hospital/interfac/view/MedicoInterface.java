package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class MedicoInterface extends JFrame {

    private final Medico medicoLogado;
    private final Hospital hospital;
    private DefaultTableModel modelInternados;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;
        
        configurarJanela();

        // Painel Superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(39, 174, 96));
        painelSuperior.setPreferredSize(new Dimension(1000, 60));
        
        JLabel lblBemVindo = new JLabel("  Dr(a). " + medicoLogado.getNome());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        // BOTÃO LOGOUT VERMELHO
        JButton btnLogout = new JButton("SAIR / LOGOUT ⤶ ");
        btnLogout.setBackground(new Color(192, 57, 43)); // VERMELHO
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("🩺 Atendimentos", criarPainelConsultasAba());
        abas.addTab("🏥 Pacientes Internados", criarPainelInternados());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Módulo Médico");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { executarSair(); }
        });
    }

    private JPanel criarPainelConsultasAba() {
        JPanel p = new JPanel(new GridBagLayout());
        JButton btnAbrirAgenda = new JButton("ABRIR AGENDA E INICIAR CONSULTAS");
        btnAbrirAgenda.setPreferredSize(new Dimension(300, 80));
        btnAbrirAgenda.addActionListener(e -> {
            AgendaMedicoView agenda = new AgendaMedicoView(medicoLogado, hospital);
            agenda.setVisible(true);
            // Sincroniza internados quando fechar a agenda
            agenda.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) { atualizarInternados(); }
            });
        });
        p.add(btnAbrirAgenda);
        return p;
    }

    private JPanel criarPainelInternados() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        modelInternados = new DefaultTableModel(new String[]{"Paciente", "CPF", "Status"}, 0);
        p.add(new JScrollPane(new JTable(modelInternados)), BorderLayout.CENTER);
        atualizarInternados();
        return p;
    }

    public void atualizarInternados() {
        modelInternados.setRowCount(0);
        for (Paciente pa : hospital.listarPacientes()) {
            if (pa.getStatusInternacao() == StatusInternacao.INTERNADO) {
                modelInternados.addRow(new Object[]{pa.getNome(), pa.getCpf(), "INTERNADO"});
            }
        }
    }

    private void executarSair() {
        if (JOptionPane.showConfirmDialog(this, "Deseja sair?", "Logout", 0) == 0) {
            dispose();
            new LoginInterface(hospital);
        }
    }
}