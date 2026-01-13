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
import javax.swing.Timer;

public class MedicoInterface extends JFrame {

    private final Medico medicoLogado;
    private final Hospital hospital;
    private DefaultTableModel modelInternados;
    private JTabbedPane abas;
    private JTable tabelaInternados;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;
        
        Timer timer = new Timer(5000, e -> atualizarInternados());
        timer.start();

        configurarJanela();

        // 1. Painel Superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(39, 174, 96));
        painelSuperior.setPreferredSize(new Dimension(1000, 60));

        JLabel lblBemVindo = new JLabel("  🩺 Dr(a). " + medicoLogado.getNome());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnLogout = new JButton("SAIR / LOGOUT ⤶ ");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> executarSair());

        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);

        // 2. Criar as Abas
        abas = new JTabbedPane();
        abas.addTab(" Fila de Atendimentos", criarPainelConsultasAba());
        abas.addTab(" Pacientes Internados", criarPainelInternados());

        // 3. Painel Central (Ações à esquerda + Abas no centro)
        JPanel pnlCorpo = new JPanel(new BorderLayout(10, 10));
        pnlCorpo.add(criarPainelAcoesRapidas(), BorderLayout.WEST);
        pnlCorpo.add(abas, BorderLayout.CENTER);

        // Adicionar tudo ao Frame principal
        add(painelSuperior, BorderLayout.NORTH);
        add(pnlCorpo, BorderLayout.CENTER);

        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Módulo Médico - " + medicoLogado.getNome());
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { executarSair(); }
        });
    }

    private JPanel criarPainelAcoesRapidas() {
        JPanel p = new JPanel(new GridLayout(6, 1, 10, 10));
        p.setPreferredSize(new Dimension(220, 0));
        p.setBorder(BorderFactory.createTitledBorder("Gestão e Agenda"));
        p.setBackground(new Color(240, 240, 240));

        JButton btnAgenda = new JButton(" Configurar Agenda");
        btnAgenda.addActionListener(e -> new ConfigurarAgendaView(this, medicoLogado).setVisible(true));

        JButton btnDados = new JButton(" Meus Dados");
        JButton btnHistorico = new JButton(" Histórico Geral");

        p.add(btnAgenda);
        p.add(btnDados);
        p.add(btnHistorico);

        return p;
    }

    private JPanel criarPainelConsultasAba() {
        JPanel p = new JPanel(new GridBagLayout());
        JButton btnAbrirAgenda = new JButton("<html><center><b>ABRIR AGENDA</b><br>Iniciar Atendimentos de Hoje</center></html>");
        btnAbrirAgenda.setPreferredSize(new Dimension(300, 80));
        btnAbrirAgenda.setBackground(new Color(41, 128, 185));
        btnAbrirAgenda.setForeground(Color.WHITE);
        
        btnAbrirAgenda.addActionListener(e -> {
            AgendaMedicoView agendaView = new AgendaMedicoView(medicoLogado, hospital);
            agendaView.setVisible(true);
            agendaView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) { atualizarInternados(); }
            });
        });
        
        p.add(btnAbrirAgenda);
        return p;
    }

    private JPanel criarPainelInternados() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adicionado a coluna do Objeto Paciente (invisível para o usuário)
        modelInternados = new DefaultTableModel(new String[]{"Paciente", "CPF", "Status", "OBJ"}, 0);
        JTable tabela = new JTable(modelInternados);
        tabela.removeColumn(tabela.getColumnModel().getColumn(3)); // Esconde a coluna do objeto
        
        atualizarInternados();

        // --- INÍCIO DA ADIÇÃO DOS BOTÕES DE ALTA E HISTÓRICO ---
        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnHistorico = new JButton("Ver Histórico/Prontuário");
        JButton btnAlta = new JButton("Dar Alta Hospitalar");
        btnAlta.setBackground(new Color(231, 76, 60)); // Vermelho
        btnAlta.setForeground(Color.WHITE);

        btnAlta.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                Paciente pac = (Paciente) modelInternados.getValueAt(row, 3);
                pac.setStatusInternacao(StatusInternacao.ALTA); // Altera o status
                atualizarInternados(); // Atualiza a tela na hora
                JOptionPane.showMessageDialog(this, "Alta registrada para " + pac.getNome());
            }
        });

        pnlAcoes.add(btnHistorico);
        pnlAcoes.add(btnAlta);
        // --- FIM DA ADIÇÃO ---

        p.add(new JLabel("Pacientes sob sua responsabilidade na internação:"), BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        p.add(pnlAcoes, BorderLayout.SOUTH);
        
        return p;
    }
    
    private void abrirHistoricoPaciente() {
        int row = tabelaInternados.getSelectedRow();
        if (row != -1) {
            Paciente p = (Paciente) modelInternados.getValueAt(row, 3);
            // Aqui você abre a view de histórico que já deve existir ou uma nova
            JOptionPane.showMessageDialog(this, "Acessando histórico de: " + p.getNome() + 
                "\nTotal de consultas: " + p.getHistorico().getConsultas().size());
        }
    }
    
    private void darAltaPaciente() {
        int row = tabelaInternados.getSelectedRow();
        if (row != -1) {
            Paciente p = (Paciente) modelInternados.getValueAt(row, 3);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar alta para " + p.getNome() + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                p.setStatusInternacao(StatusInternacao.ALTA); 
                atualizarInternados();
            }
        }
    }

    public void atualizarInternados() {
        if (modelInternados == null) return;
        modelInternados.setRowCount(0);
        for (Paciente p : hospital.listarPacientes()) {
            if (p.getStatusInternacao() == StatusInternacao.INTERNADO) {
                modelInternados.addRow(new Object[]{p.getNome(), p.getCpf(), p.getStatusInternacao(), p});
            }
        }
    }

    private void executarSair() {
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente fazer logout?", "Sair", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }
}