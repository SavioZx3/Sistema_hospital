package ufjf.dcc025.hospital.interfac.view.paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.servicos_hospital.consulta.*;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.*;

public class PacienteInterface extends BaseView {

    private final Paciente paciente;
    private DefaultTableModel modelAgenda, modelNovoAgendamento, modelHistorico;

    public PacienteInterface(Hospital hospital, Paciente paciente) {
        super(hospital, "Portal do Paciente");
        this.paciente = paciente;

        setLayout(new BorderLayout());

        add(criarTopoPaciente(), BorderLayout.NORTH);
        add(criarAbas(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ======================= TOPO =======================

    private JPanel criarTopoPaciente() {
        JPanel topo = criarTopo("Paciente: " + paciente.getNome());

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> executarLogout());

        topo.add(btnLogout, BorderLayout.EAST);
        return topo;
    }

    // ======================= ABAS =======================

    private JTabbedPane criarAbas() {
        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Minhas Consultas", criarPainelMinhaAgenda());
        abas.addTab("Novo Agendamento", criarPainelNovoAgendamento());
        abas.addTab("Histórico", criarPainelHistorico());
        abas.addTab("Meus Dados", criarPainelMeusDados());
        abas.addTab("Visitas", criarPainelVisitas());

        return abas;
    }

    // ======================= MEUS DADOS =======================

    private JPanel criarPainelMeusDados() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtEmail = new JTextField(paciente.getEnderecoEmail(), 25);
        JTextField txtRua = new JTextField(paciente.getEndereco().getRua(), 20);
        JTextField txtNum = new JTextField(paciente.getEndereco().getNumero(), 5);
        JTextField txtBairro = new JTextField(paciente.getEndereco().getBairro(), 15);
        JTextField txtCidade = new JTextField(paciente.getEndereco().getCidade(), 15);

        JPasswordField txtSenhaAtual = new JPasswordField(20);
        JPasswordField txtNovaSenha = new JPasswordField(20);

        adicionarCampo(p, "E-mail:", txtEmail, g, 0);
        adicionarCampo(p, "Rua:", txtRua, g, 1);
        adicionarCampo(p, "Número:", txtNum, g, 2);
        adicionarCampo(p, "Bairro:", txtBairro, g, 3);
        adicionarCampo(p, "Cidade:", txtCidade, g, 4);
        adicionarCampo(p, "Senha Atual:", txtSenhaAtual, g, 5);
        adicionarCampo(p, "Nova Senha:", txtNovaSenha, g, 6);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.setPreferredSize(new Dimension(200, 40));

        btnSalvar.addActionListener(e -> {
            try {
                if (!paciente.validarSenha(new String(txtSenhaAtual.getPassword()))) {
                    throw new Exception("Senha atual incorreta");
                }

                paciente.setEndereco(
                        txtRua.getText(),
                        txtNum.getText(),
                        txtBairro.getText(),
                        txtCidade.getText()
                );

                paciente.setEmail(txtEmail.getText());

                if (!new String(txtNovaSenha.getPassword()).isEmpty()) {
                    paciente.setSenha(new String(txtNovaSenha.getPassword()));
                }

                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
                txtSenhaAtual.setText("");
                txtNovaSenha.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        p.add(btnSalvar, g);

        return p;
    }

    private void adicionarCampo(JPanel p, String rotulo, JComponent campo, GridBagConstraints g, int linha) {
        g.gridx = 0; g.gridy = linha;
        p.add(new JLabel(rotulo), g);
        g.gridx = 1;
        p.add(campo, g);
    }

    // ======================= CONSULTAS =======================

    private JPanel criarPainelMinhaAgenda() {
        JPanel p = new JPanel(new BorderLayout());
        modelAgenda = new DefaultTableModel(new String[]{"Data", "Hora", "Médico", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelAgenda);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnCancelar = new JButton("Cancelar Consulta");
        btnCancelar.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                hospital.cancelarAgendamento((Consulta) modelAgenda.getValueAt(row, 4));
                atualizarTabelas();
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnCancelar, BorderLayout.SOUTH);
        atualizarMinhaAgenda();

        return p;
    }

    private JPanel criarPainelNovoAgendamento() {
        JPanel p = new JPanel(new BorderLayout());
        modelNovoAgendamento = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Data", "Hora", "OBJ"}, 0);
        JTable t = new JTable(modelNovoAgendamento);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnAgendar = new JButton("Agendar Consulta");
        btnAgendar.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) modelNovoAgendamento.getValueAt(row, 4);
                hospital.agendarConsultaNaAgenda(paciente, c.getMedico(), c);
                atualizarTabelas();
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnAgendar, BorderLayout.SOUTH);
        atualizarNovoAgendamento();

        return p;
    }

    private JPanel criarPainelHistorico() {
        JPanel p = new JPanel(new BorderLayout());
        modelHistorico = new DefaultTableModel(new String[]{"Data", "Médico", "Status"}, 0);
        p.add(new JScrollPane(new JTable(modelHistorico)), BorderLayout.CENTER);
        atualizarHistorico();
        return p;
    }

    private JPanel criarPainelVisitas() {
        return new VisitasPacienteView(hospital);
    }

    // ======================= ATUALIZAÇÕES =======================

    private void atualizarTabelas() {
        atualizarMinhaAgenda();
        atualizarNovoAgendamento();
        atualizarHistorico();
    }

    private void atualizarMinhaAgenda() {
        modelAgenda.setRowCount(0);
        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (paciente.equals(c.getPaciente()) && c.getStatus() == StatusConsulta.AGENDADA) {
                    modelAgenda.addRow(new Object[]{
                            c.getData(),
                            c.getDataHoraInicio().toLocalTime(),
                            m.getNome(),
                            c.getStatus(),
                            c
                    });
                }
            }
        }
    }

private void atualizarNovoAgendamento() {
    modelNovoAgendamento.setRowCount(0);

    for (Medico m : hospital.listarMedicos()) {

        // 1. Ignora médicos inativos
        if (!m.isAtivo()) {
            continue;
        }

        for (Consulta c : m.getAgenda().getConsultas()) {

            // 2. Apenas consultas disponíveis
            if (c.getStatus() != StatusConsulta.DISPONIVEL) {
                continue;
            }

            modelNovoAgendamento.addRow(new Object[]{
                m.getNome(),
                m.getEspecialidade(),
                c.getData(),
                c.getDataHoraInicio().toLocalTime(),
                c
            });
        }
    }
}

    private void atualizarHistorico() {
        modelHistorico.setRowCount(0);
        for (Consulta c : paciente.getHistorico().getConsultas()) {
            modelHistorico.addRow(new Object[]{
                    c.getData(),
                    c.getMedico().getNome(),
                    c.getStatus()
            });
        }
    }

    // ======================= RODAPÉ =======================

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Status: Paciente ativo"));
        return p;
    }
}
