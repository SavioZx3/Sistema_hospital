package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class PacienteInterface extends JFrame {

    private final Paciente pacienteLogado;
    private final Hospital hospital;
    private DefaultTableModel modelAgenda;
    private DefaultTableModel modelNovoAgendamento;
    private DefaultTableModel modelHistorico;

    public PacienteInterface(Paciente paciente, Hospital hospital) {
        this.pacienteLogado = paciente;
        this.hospital = hospital;

        configurarJanela();
        configurarMenu();

        // Painel Superior com Botão Logout Explícito
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(236, 240, 241));
        JLabel lblBemVindo = new JLabel("  Bem-vindo(a), " + pacienteLogado.getNome());
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton btnLogout = new JButton("Sair / Logout ⤶ ");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        // Abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab(" Minhas Consultas", criarPainelMinhaAgenda());
        abas.addTab(" Agendar Nova", criarPainelNovoAgendamento());
        abas.addTab(" Meu Histórico", criarPainelHistorico());
        abas.addTab(" Meus Dados", criarPainelMeusDados()); 
        abas.addTab(" Visitas", criarPainelVisitas());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Portal do Paciente");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { executarSair(); }
        });
    }

    private JPanel criarPainelMeusDados() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos de texto com dados atuais
        JTextField txtEmail = new JTextField(pacienteLogado.getEmail(), 20);
        JPasswordField txtSenha = new JPasswordField(" ****** ",20);
        
        // Campos de Endereço (Separados para evitar o erro de compilação)
        JTextField txtRua = new JTextField(pacienteLogado.getEndereco().getRua(),20);
        JTextField txtNum = new JTextField(pacienteLogado.getEndereco().getNumero(),5);
        JTextField txtBairro = new JTextField(pacienteLogado.getEndereco().getBairro(),15);
        JTextField txtCidade = new JTextField( pacienteLogado.getEndereco().getCidade(),15);

        JTextField txtCpf = new JTextField(pacienteLogado.getCpf());
        txtCpf.setEditable(false);
        txtCpf.setBackground(new Color(230, 230, 230));
        
        JTextField txtNome = new JTextField(pacienteLogado.getNome());
        txtNome.setEditable(false);
        txtNome.setBackground(new Color(230, 230, 230));
        

        // Montagem do Layout
        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; p.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; p.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Rua:"), gbc);
        gbc.gridx = 1; p.add(txtRua, gbc);

        gbc.gridx = 0; gbc.gridy = 3; p.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; p.add(txtNum, gbc);

        gbc.gridx = 0; gbc.gridy = 4; p.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1; p.add(txtBairro, gbc);

        gbc.gridx = 0; gbc.gridy = 5; p.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; p.add(txtCidade, gbc);

        gbc.gridx = 0; gbc.gridy = 6; p.add(new JLabel("Senha:" ), gbc);
        gbc.gridx = 1; p.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 7; p.add(new JLabel("CPF (Fixo):"), gbc);
        gbc.gridx = 1; p.add(txtCpf, gbc);

        JButton btnSalvar = new JButton(" Salvar Alterações");
        btnSalvar.setBackground(new Color(41, 128, 185));
        btnSalvar.setForeground(Color.WHITE);
        

        
        btnSalvar.addActionListener(e -> {
            try {
                pacienteLogado.setNome(txtNome.getText());
                pacienteLogado.setEmail(txtEmail.getText());
                pacienteLogado.setSenha(new String(txtSenha.getPassword()));
                
                pacienteLogado.setEndereco(
                    txtRua.getText(), 
                    txtNum.getText(), 
                    txtBairro.getText(), 
                    txtCidade.getText()
                );

                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
                setTitle("Portal do Paciente - " + pacienteLogado.getNome());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        p.add(btnSalvar, gbc);

        return p;
    }

    private void configurarMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Arquivo");
        JMenuItem miSair = new JMenuItem("Sair");
        miSair.addActionListener(e -> executarSair());
        m.add(miSair);
        mb.add(m);
        setJMenuBar(mb);
    }

    private void executarSair() {
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }

    private JPanel criarPainelMinhaAgenda() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        modelAgenda = new DefaultTableModel(new String[]{"Data", "Hora", "Médico", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelAgenda);
        t.removeColumn(t.getColumnModel().getColumn(4));
        
        JButton btnCanc = new JButton("Cancelar Consulta Selecionada");
        btnCanc.addActionListener(e -> {
            int row = t.getSelectedRow();
            if(row != -1) {
                hospital.cancelarAgendamento((Consulta) modelAgenda.getValueAt(row, 4));
                atualizarTodasTabelas();
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnCanc, BorderLayout.SOUTH);
        atualizarMinhaAgenda();
        return p;
    }

    private JPanel criarPainelNovoAgendamento() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        modelNovoAgendamento = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Data", "Hora", "OBJ"}, 0);
        JTable t = new JTable(modelNovoAgendamento);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnAg = new JButton("Confirmar Agendamento");
        btnAg.addActionListener(e -> {
            int row = t.getSelectedRow();
            if(row != -1) {
                Consulta c = (Consulta) modelNovoAgendamento.getValueAt(row, 4);
                hospital.agendarConsultaNaAgenda(pacienteLogado, c.getMedico(), c);
                atualizarTodasTabelas();
                JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnAg, BorderLayout.SOUTH);
        atualizarNovoAgendamento();
        return p;
    }

    private JPanel criarPainelHistorico() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        modelHistorico = new DefaultTableModel(new String[]{"Data", "Médico", "Status Final"}, 0);
        JTable t = new JTable(modelHistorico);
        
        p.add(new JLabel("Seu histórico completo de consultas:"), BorderLayout.NORTH);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        atualizarHistorico();
        return p;
    }

    private JPanel criarPainelVisitas() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JTextField txtBusca = new JTextField();
        JTextArea res = new JTextArea();
        res.setEditable(false);
        res.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        JButton btnBusca = new JButton("Buscar por Nome ou CPF");
        btnBusca.addActionListener(e -> {
            String busca = txtBusca.getText().toLowerCase();
            StringBuilder sb = new StringBuilder();
            boolean encontrou = false;

            for (Paciente pac : hospital.listarPacientes()) {
                if (pac.getStatusInternacao() == StatusInternacao.INTERNADO) {
                    if (pac.getNome().toLowerCase().contains(busca) || pac.getCpf().contains(busca)) {
                        sb.append("PACIENTE: ").append(pac.getNome()).append("\n");
                        sb.append("STATUS: ").append(pac.getStatusVisita().getDescricao()).append("\n");
                        sb.append("------------------------------------------\n");
                        encontrou = true;
                    }
                }
            }

            if (!encontrou) {
                res.setText("\n\n   Nenhum paciente internado encontrado ou sem visitas permitidas.");
            } else {
                res.setText(sb.toString());
            }
        });

        JPanel norte = new JPanel(new GridLayout(2, 1, 5, 5));
        norte.add(new JLabel("Digite o Nome ou CPF do paciente internado:"));
        norte.add(txtBusca);

        p.add(norte, BorderLayout.NORTH);
        p.add(new JScrollPane(res), BorderLayout.CENTER);
        p.add(btnBusca, BorderLayout.SOUTH);
        return p;
    }

    private void atualizarTodasTabelas() {
        atualizarMinhaAgenda();
        atualizarNovoAgendamento();
        atualizarHistorico();
    }

    private void atualizarMinhaAgenda() {
        modelAgenda.setRowCount(0);
        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (pacienteLogado.equals(c.getPaciente()) && c.getStatus() == StatusConsulta.AGENDADA) {
                    modelAgenda.addRow(new Object[]{c.getData(), c.getDataHoraInicio().toLocalTime(), m.getNome(), c.getStatus(), c});
                }
            }
        }
    }

    private void atualizarNovoAgendamento() {
        modelNovoAgendamento.setRowCount(0);
        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (c.getStatus() == StatusConsulta.DISPONIVEL) {
                    modelNovoAgendamento.addRow(new Object[]{m.getNome(), m.getEspecialidade(), c.getData(), c.getDataHoraInicio().toLocalTime(), c});
                }
            }
        }
    }

    private void atualizarHistorico() {
        modelHistorico.setRowCount(0);
        List<Consulta> lista = pacienteLogado.getHistorico().getConsultas();
        for (Consulta c : lista) {
            modelHistorico.addRow(new Object[]{c.getData(), c.getMedico().getNome(), c.getStatus()});
        }
    }
}