package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;
import ufjf.dcc025.hospital.usuarios.paciente.StatusVisita;

public class MedicoInterface extends JFrame {

    private final Medico medicoLogado;
    private final Hospital hospital;
    private DefaultTableModel modelConsultas;
    private DefaultTableModel modelInternados;
    private JComboBox<String> comboFiltro;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;
        
        configurarJanela();
        configurarMenu();

        // Painel Superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(39, 174, 96));
        
        JLabel lblBemVindo = new JLabel("  Painel Médico - Dr(a). " + medicoLogado.getNome());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton btnLogout = new JButton("Sair / Logout ⤶ ");
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("🩺 Atendimentos", criarPainelConsultas());
        abas.addTab("📅 Agenda Semanal", criarPainelConfiguracaoAgenda());
        abas.addTab("🏥 Pacientes Internados", criarPainelVisitas());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema Hospitalar - Módulo Médico");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { executarSair(); }
        });
    }

    private void configurarMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Sessão");
        JMenuItem sair = new JMenuItem("Logout");
        sair.addActionListener(e -> executarSair());
        m.add(sair);
        mb.add(m);
        setJMenuBar(mb);
    }

    private void executarSair() {
        if (JOptionPane.showConfirmDialog(this, "Deseja encerrar a sessão?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }

    private JPanel criarPainelConsultas() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Filtro Superior
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Visualizar:"));
        comboFiltro = new JComboBox<>(new String[]{"Todas as Consultas", "Apenas Disponíveis", "Apenas Agendadas"});
        comboFiltro.addActionListener(e -> atualizarConsultas());
        painelFiltro.add(comboFiltro);

        modelConsultas = new DefaultTableModel(new String[]{"Paciente", "Data", "Hora", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelConsultas);
        t.removeColumn(t.getColumnModel().getColumn(4)); // Esconde o objeto Consulta

        JButton btnAtender = new JButton("🚀 INICIAR ATENDIMENTO");
        btnAtender.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAtender.setBackground(new Color(41, 128, 185));
        btnAtender.setForeground(Color.WHITE);
        
        btnAtender.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) modelConsultas.getValueAt(row, 4);

                // Verificamos se a consulta já foi realizada para evitar "re-atendimento"
                if (c.getStatus() == StatusConsulta.REALIZADA) {
                    JOptionPane.showMessageDialog(this, "Esta consulta já foi finalizada e não pode ser editada.");
                    return;
                }

                if (c.getStatus() == StatusConsulta.AGENDADA || c.getStatus() == StatusConsulta.EM_ANDAMENTO) {
                    AtendimentoMedicoView telaAtendimento = new AtendimentoMedicoView(this, medicoLogado, c);
                    telaAtendimento.setVisible(true);

                    // Atualiza a tabela após fechar a janela de atendimento
                    atualizarConsultas();
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione uma consulta AGENDADA para iniciar.");
                }
            }
        });

        p.add(painelFiltro, BorderLayout.NORTH);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnAtender, BorderLayout.SOUTH);
        
        atualizarConsultas();
        return p;
    }

    public void atualizarConsultas() {
        modelConsultas.setRowCount(0);
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        String filtro = (String) comboFiltro.getSelectedItem();

        for (Consulta c : medicoLogado.getAgenda().getConsultas()) {
            boolean exibir = false;
            if (filtro.equals("Todas as Consultas")) exibir = true;
            else if (filtro.equals("Apenas Disponíveis") && c.getStatus() == StatusConsulta.DISPONIVEL) exibir = true;
            else if (filtro.equals("Apenas Agendadas") && c.getStatus() == StatusConsulta.AGENDADA) exibir = true;

            if (exibir) {
                modelConsultas.addRow(new Object[]{
                    c.getPaciente() != null ? c.getPaciente().getNome() : "---", 
                    c.getData(), 
                    c.getDataHoraInicio().format(hm), 
                    c.getStatus(), 
                    c
                });
            }
        }
    }

    private JPanel criarPainelVisitas() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        modelInternados = new DefaultTableModel(new String[]{"Paciente", "CPF", "Visitas Permitidas?", "OBJ"}, 0);
        JTable t = new JTable(modelInternados);
        t.removeColumn(t.getColumnModel().getColumn(3));

        JButton btnAlternar = new JButton("Alternar Permissão de Visita");
        btnAlternar.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                Paciente pac = (Paciente) modelInternados.getValueAt(row, 3);
                pac.setStatusVisita(pac.getStatusVisita() == StatusVisita.APTO ? StatusVisita.INAPTO : StatusVisita.APTO);
                atualizarInternados();
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnAlternar, BorderLayout.SOUTH);
        atualizarInternados();
        return p;
    }

    private void atualizarInternados() {
        modelInternados.setRowCount(0);
        for (Paciente pa : hospital.listarPacientes()) {
            if (pa.getStatusInternacao() == StatusInternacao.INTERNADO) {
                modelInternados.addRow(new Object[]{pa.getNome(), pa.getCpf(), pa.getStatusVisita(), pa});
            }
        }
    }

    private JPanel criarPainelConfiguracaoAgenda() {
        JPanel p = new JPanel(new FlowLayout());
        JButton btnGerar = new JButton("Gerar Slots da Próxima Semana");
        btnGerar.addActionListener(e -> {
            medicoLogado.getAgenda().gerarConsultasProximaSemana();
            atualizarConsultas();
            JOptionPane.showMessageDialog(this, "Horários gerados com sucesso!");
        });
        p.add(btnGerar);
        return p;
    }
}