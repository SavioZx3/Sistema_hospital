package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
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

    public PacienteInterface(Paciente paciente, Hospital hospital) {
        this.pacienteLogado = paciente;
        this.hospital = hospital;

        setTitle("Portal do Paciente - " + pacienteLogado.getNome());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarMenu();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("️ Minhas Consultas", criarPainelMinhaAgenda());
        abas.addTab(" Agendar Nova", criarPainelNovoAgendamento());
        abas.addTab(" Documentos", criarPainelDocumentos());
        abas.addTab(" Consultar Visitas", criarPainelConsultaVisitas());

        add(abas);
    }

    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Minha Conta");
        
        JMenuItem itemDados = new JMenuItem("Editar Perfil");
        JMenuItem itemSair = new JMenuItem("Sair / Logout");

        itemDados.addActionListener(e -> abrirEdicaoDados());
        itemSair.addActionListener(e -> {
            dispose();
            new LoginInterface(hospital).setVisible(true);
        });

        menuSessao.add(itemDados);
        menuSessao.addSeparator();
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);
    }

    private JPanel criarPainelMinhaAgenda() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelAgenda = new DefaultTableModel(new String[]{"Data", "Hora", "Médico", "Especialidade", "Status", "OBJ"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(modelAgenda);
        tabela.removeColumn(tabela.getColumnModel().getColumn(5)); // Esconde o objeto Consulta

        JButton btnCancelar = new JButton(" Cancelar Consulta Selecionada");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        
        btnCancelar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) modelAgenda.getValueAt(row, 5);
                if (c.getStatus() == StatusConsulta.AGENDADA) {
                    c.cancelarConsulta();
                    JOptionPane.showMessageDialog(this, "Cancelada!");
                    atualizarTabelas();
                }
            }
        });

        atualizarMinhaAgenda();
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnCancelar, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelNovoAgendamento() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelNovoAgendamento = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Data", "Hora", "OBJ"}, 0);
        JTable tabela = new JTable(modelNovoAgendamento);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4));

        JButton btnAgendar = new JButton(" Confirmar Agendamento");
        btnAgendar.setBackground(new Color(46, 204, 113));
        btnAgendar.setForeground(Color.WHITE);

        btnAgendar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) modelNovoAgendamento.getValueAt(row, 4);
                try {
                    c.agendar(pacienteLogado);
                    JOptionPane.showMessageDialog(this, "Agendado!");
                    atualizarTabelas();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });

        atualizarNovoAgendamento();
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnAgendar, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelDocumentos() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        JButton btnRec = new JButton(" Receitas");
        JButton btnAtest = new JButton(" Atestados");
        JButton btnExame = new JButton(" Exames");

        btnRec.addActionListener(e -> exibirLista("Receitas", pacienteLogado.getReceitas().toArray()));
        btnAtest.addActionListener(e -> exibirLista("Atestados", pacienteLogado.getAtestados().toArray()));
        btnExame.addActionListener(e -> exibirLista("Exames", pacienteLogado.getExames().toArray()));

        painel.add(btnRec); painel.add(btnAtest); painel.add(btnExame);
        return painel;
    }

    private JPanel criarPainelConsultaVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField txtBusca = new JTextField();
        JButton btnBuscar = new JButton(" Consultar Status");
        JTextArea txtResultado = new JTextArea(8, 20);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));

        btnBuscar.addActionListener(e -> {
            String nome = txtBusca.getText().toLowerCase();
            StringBuilder sb = new StringBuilder();
            hospital.listarPacientes().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome) && p.getStatusInternacao() == StatusInternacao.INTERNADO)
                .forEach(p -> sb.append("Paciente: ").append(p.getNome())
                               .append("\nStatus Visita: ").append(p.getStatusVisita().getDescricao()).append("\n\n"));
            
            txtResultado.setText(sb.length() > 0 ? sb.toString() : "Nenhum paciente internado encontrado com este nome.");
        });

        JPanel pnlTopo = new JPanel(new BorderLayout());
        pnlTopo.add(new JLabel("Nome do Paciente:"), BorderLayout.NORTH);
        pnlTopo.add(txtBusca, BorderLayout.CENTER);
        pnlTopo.add(btnBuscar, BorderLayout.SOUTH);

        painel.add(pnlTopo, BorderLayout.NORTH);
        painel.add(new JScrollPane(txtResultado), BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTabelas() {
        atualizarMinhaAgenda();
        atualizarNovoAgendamento();
    }

    private void atualizarMinhaAgenda() {
        modelAgenda.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (pacienteLogado.equals(c.getPaciente())) {
                    modelAgenda.addRow(new Object[]{c.getData(), c.getDataHoraInicio().format(dtf), m.getNome(), m.getEspecialidade(), c.getStatus(), c});
                }
            }
        }
    }

    private void atualizarNovoAgendamento() {
        modelNovoAgendamento.setRowCount(0);
        for (Medico m : hospital.listarMedicos()) {
            if (m.isAtivo()) {
                for (Consulta c : m.getAgenda().getConsultas()) {
                    if (c.getStatus() == StatusConsulta.DISPONIVEL) {
                        modelNovoAgendamento.addRow(new Object[]{m.getNome(), m.getEspecialidade(), c.getData(), c.getDataHoraInicio().toLocalTime(), c});
                    }
                }
            }
        }
    }

    private void abrirEdicaoDados() {
        String novoEmail = JOptionPane.showInputDialog(this, "Atualizar E-mail:", pacienteLogado.getEmail());
        if (novoEmail != null) pacienteLogado.setEmail(novoEmail);
    }

    private void exibirLista(String titulo, Object[] dados) {
        JDialog d = new JDialog(this, titulo, true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        JList<Object> lista = new JList<>(dados);
        d.add(new JScrollPane(lista));
        d.setVisible(true);
    }
}