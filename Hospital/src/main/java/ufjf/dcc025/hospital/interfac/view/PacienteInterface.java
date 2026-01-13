package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private DefaultTableModel modelAgenda, modelNovoAgendamento, modelHistorico;

    public PacienteInterface(Paciente paciente, Hospital hospital) {
        this.pacienteLogado = paciente;
        this.hospital = hospital;

        configurarJanela();

        // --- PAINEL SUPERIOR ---
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(44, 62, 80));
        painelSuperior.setPreferredSize(new Dimension(1000, 60));
        
        JLabel lblBemVindo = new JLabel("   Portal do Paciente: " + pacienteLogado.getNome());
        lblBemVindo.setForeground(Color.WHITE);
        lblBemVindo.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JButton btnLogout = new JButton("Sair / Logout ⤶ ");
        btnLogout.setBackground(new Color(192, 57, 43)); // Vermelho
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lblBemVindo, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        // --- ABAS ---
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("🗓 Minhas Consultas", criarPainelMinhaAgenda());
        abas.addTab("➕ Agendar Nova", criarPainelNovoAgendamento());
        abas.addTab("📜 Meu Histórico", criarPainelHistorico());
        abas.addTab("👤 Meus Dados", criarPainelMeusDados());
        abas.addTab("👥 Buscar Internados", criarPainelVisitas());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Hospital - Área do Paciente");
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
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Campos Editáveis
        JTextField txtEmail = new JTextField(pacienteLogado.getEmail().toString(), 25);
        JTextField txtRua = new JTextField(pacienteLogado.getEndereco().getRua(), 20);
        JTextField txtNum = new JTextField(pacienteLogado.getEndereco().getNumero(), 5);
        JTextField txtBairro = new JTextField(pacienteLogado.getEndereco().getBairro(), 15);
        JTextField txtCidade = new JTextField(pacienteLogado.getEndereco().getCidade(), 15);
        
        // Senhas
        JPasswordField txtSenhaAtual = new JPasswordField(20);
        JPasswordField txtNovaSenha = new JPasswordField(20);

        // Layout
        adicionarCampo(p, "E-mail:", txtEmail, g, 0);
        adicionarCampo(p, "Rua:", txtRua, g, 1);
        adicionarCampo(p, "Número:", txtNum, g, 2);
        adicionarCampo(p, "Bairro:", txtBairro, g, 3);
        adicionarCampo(p, "Cidade:", txtCidade, g, 4);
        adicionarCampo(p, "Senha Atual:", txtSenhaAtual, g, 5);
        adicionarCampo(p, "Nova Senha :", txtNovaSenha, g, 6);

        JButton btnSalvar = new JButton("💾 SALVAR ALTERAÇÕES");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(41, 128, 185));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(0, 45));

        btnSalvar.addActionListener(e -> {
            String senhaDigitada = new String(txtSenhaAtual.getPassword());
            String novaSenha = new String(txtNovaSenha.getPassword());

            try {
                // 1. Validação de Segurança
                if (!pacienteLogado.validarSenha(senhaDigitada)) {
                    throw new Exception("Senha atual incorreta!");
                }

                // 2. Atualização de Endereço
                pacienteLogado.setEndereco(txtRua.getText(), txtNum.getText(), txtBairro.getText(), txtCidade.getText());

                // 3. Atualização de E-mail
                pacienteLogado.setEmail(txtEmail.getText());

                // 4. Atualização de Senha
                if (!novaSenha.isEmpty()) {
                    pacienteLogado.setSenha(novaSenha);
                }

                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
                txtSenhaAtual.setText("");
                txtNovaSenha.setText("");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Falha ao Salvar", JOptionPane.ERROR_MESSAGE);
            }
        });

        g.gridx = 0; g.gridy = 7; g.gridwidth = 2; g.insets = new Insets(20, 0, 0, 0);
        p.add(btnSalvar, g);

        return p;
    }

    private void adicionarCampo(JPanel p, String rotulo, JComponent campo, GridBagConstraints g, int linha) {
        g.gridwidth = 1;
        g.gridx = 0; g.gridy = linha;
        p.add(new JLabel(rotulo), g);
        g.gridx = 1;
        p.add(campo, g);
    }

    private void executarSair() {
        if (JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }

    // --- PAINÉIS DE TABELAS ---

    private JPanel criarPainelMinhaAgenda() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        modelAgenda = new DefaultTableModel(new String[]{"Data", "Hora", "Médico", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelAgenda);
        t.removeColumn(t.getColumnModel().getColumn(4));
        
        JButton btnCanc = new JButton("❌ Cancelar Consulta Selecionada");
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
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        modelNovoAgendamento = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Data", "Hora", "OBJ"}, 0);
        JTable t = new JTable(modelNovoAgendamento);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnAg = new JButton("✅ Confirmar Agendamento");
        btnAg.setBackground(new Color(39, 174, 96));
        btnAg.setForeground(Color.WHITE);
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
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        modelHistorico = new DefaultTableModel(new String[]{"Data", "Médico", "Status Final"}, 0);
        p.add(new JLabel("Seu histórico completo de consultas concluídas:"), BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(modelHistorico)), BorderLayout.CENTER);
        atualizarHistorico();
        return p;
    }

    private JPanel criarPainelVisitas() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        JTextField txtBusca = new JTextField();
        JTextArea res = new JTextArea();
        res.setEditable(false);
        res.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        JButton btnBusca = new JButton("🔍 Buscar Status de Visita");
        btnBusca.addActionListener(e -> {
            String busca = txtBusca.getText().toLowerCase();
            StringBuilder sb = new StringBuilder();
            boolean encontrou = false;

            for (Paciente pac : hospital.listarPacientes()) {
                if (pac.getStatusInternacao() == StatusInternacao.INTERNADO) {
                    if (pac.getNome().toLowerCase().contains(busca) || pac.getCpf().contains(busca)) {
                        sb.append("PACIENTE: ").append(pac.getNome()).append("\n");
                        sb.append("STATUS:   ").append(pac.getStatusVisita().getDescricao()).append("\n");
                        sb.append("------------------------------------------\n");
                        encontrou = true;
                    }
                }
            }
            res.setText(encontrou ? sb.toString() : "\nNenhum paciente internado encontrado com este critério.");
        });

        JPanel norte = new JPanel(new GridLayout(2, 1, 5, 5));
        norte.add(new JLabel("Nome ou CPF do Internado:"));
        norte.add(txtBusca);

        p.add(norte, BorderLayout.NORTH);
        p.add(new JScrollPane(res), BorderLayout.CENTER);
        p.add(btnBusca, BorderLayout.SOUTH);
        return p;
    }

    // --- MÉTODOS DE ATUALIZAÇÃO ---

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
        for (Consulta c : pacienteLogado.getHistorico().getConsultas()) {
            modelHistorico.addRow(new Object[]{c.getData(), c.getMedico().getNome(), c.getStatus()});
        }
    }
}