package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class MedicoInterface extends JFrame {
    private final Hospital hospital;
    private final Medico medicoLogado;
    private JTabbedPane abas;
    
    // Paineis das abas
    private VisitasMedicoView painelVisitas;
    private JPanel painelAgenda; // Painel que conterá a lista de consultas do dia

    public MedicoInterface(Hospital hospital, Medico medico) {
        this.hospital = hospital;
        this.medicoLogado = medico;
        
        setTitle("Sistema Hospitalar - Módulo Médico: " + medico.getNome());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        initComponents();
        setupAutoUpdate(); 
    }

    private void initComponents() {
        abas = new JTabbedPane();

        
        painelAgenda = new JPanel(new BorderLayout());
        JButton btnConfigAgenda = new JButton("Configurar Disponibilidade Semanal");
        btnConfigAgenda.addActionListener(e -> {
            new ConfigurarAgendaView(this, medicoLogado).setVisible(true);
        });
        painelAgenda.add(btnConfigAgenda, BorderLayout.NORTH);
        // painelAgenda.add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);

        // Aba 2: Visitas e Internação (A classe que corrigimos)
        painelVisitas = new VisitasMedicoView(hospital, medicoLogado);

        abas.addTab("Consultas do Dia", new ImageIcon(), painelAgenda, "Ver agenda e atender pacientes");
        abas.addTab("Visitas (Internados)", new ImageIcon(), painelVisitas, "Gerenciar pacientes internados");

        add(abas, BorderLayout.CENTER);

        // Barra inferior com status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.add(new JLabel("Médico: " + medicoLogado.getNome() + " | Especialidade: " + medicoLogado.getEspecialidade()));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupAutoUpdate() {
        // Atualiza a tabela de visitas a cada 30 segundos
        Timer timer = new Timer(5000, e -> {
            painelVisitas.popularTabela();
            // atualizarTabelaConsultas();
        });
        timer.start();
    }
}