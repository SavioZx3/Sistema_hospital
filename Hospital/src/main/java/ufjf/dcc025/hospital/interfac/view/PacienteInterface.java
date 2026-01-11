package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    public PacienteInterface(Paciente paciente, Hospital hospital) {
        this.pacienteLogado = paciente;
        this.hospital = hospital;

        setTitle("Portal do Paciente - " + pacienteLogado.getNome());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Barra de Menu para Logout e Perfil
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Minha Conta");
        JMenuItem itemDados = new JMenuItem("Editar Meus Dados");
        JMenuItem itemSair = new JMenuItem("Sair");
        
        // Requisito: Consultar Histórico e Dados Pessoais (Edição)
        itemDados.addActionListener(e -> abrirEdicaoDados());
        itemSair.addActionListener(e -> { dispose(); new LoginInterface(hospital).setVisible(true); });
        
        menuSessao.add(itemDados);
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minhas Consultas", criarPainelMinhaAgenda());
        abas.addTab("Agendar Nova Consulta", criarPainelNovoAgendamento());
        abas.addTab("Documentos Médicos", criarPainelDocumentos());
        abas.addTab("Consultar Visitas", criarPainelConsultaVisitas());

        add(abas);
        setVisible(true);
    }

    // --- REQUISITO: Gerenciar Agendamentos (Visualizar e Cancelar) ---
    private JPanel criarPainelMinhaAgenda() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        String[] colunas = {"Data", "Hora", "Médico", "Especialidade", "Status", "Objeto"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = new JTable(model);
        tabela.removeColumn(tabela.getColumnModel().getColumn(5));

        JButton btnCancelar = new JButton("Cancelar Consulta Selecionada");
        btnCancelar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) model.getValueAt(row, 5);
                if (c.getStatus() == StatusConsulta.AGENDADA) {
                    c.cancelarConsulta();
                    JOptionPane.showMessageDialog(this, "Consulta cancelada com sucesso.");
                    atualizarMinhaAgenda(model);
                }
            }
        });

        atualizarMinhaAgenda(model);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnCancelar, BorderLayout.SOUTH);
        return painel;
    }

    private void atualizarMinhaAgenda(DefaultTableModel model) {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        // Filtra consultas do hospital que pertencem a este paciente
        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (pacienteLogado.equals(c.getPaciente())) {
                    model.addRow(new Object[]{c.getData(), c.getDataHoraInicio().format(dtf), 
                        m.getNome(), m.getEspecialidade(), c.getStatus(), c});
                }
            }
        }
    }

    // --- REQUISITO: Gerenciar Agendamentos (Agendar Autonomamente) ---
    private JPanel criarPainelNovoAgendamento() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Data", "Hora", "Objeto"}, 0);
        JTable tabela = new JTable(model);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4));

        JButton btnAgendar = new JButton("Confirmar Agendamento");
        btnAgendar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) model.getValueAt(row, 4);
                try {
                    c.agendar(pacienteLogado);
                    JOptionPane.showMessageDialog(this, "Agendado com sucesso!");
                    // Atualiza ambas as tabelas
                    model.removeRow(row);
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });

        // Carregar slots DISPONÍVEIS de todos os médicos ativos
        for (Medico m : hospital.listarMedicos()) {
            if (m.isAtivo()) {
                for (Consulta c : m.getAgenda().getConsultas()) {
                    if (c.getStatus() == StatusConsulta.DISPONIVEL) {
                        model.addRow(new Object[]{m.getNome(), m.getEspecialidade(), c.getData(), 
                            c.getDataHoraInicio().toLocalTime(), c});
                    }
                }
            }
        }

        painel.add(new JLabel("Selecione um horário disponível:"), BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(btnAgendar, BorderLayout.SOUTH);
        return painel;
    }

    // --- REQUISITO: Acessar Documentos Médicos (Centralizado) ---
    private JPanel criarPainelDocumentos() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        JButton btnRec = new JButton("Ver Receitas");
        btnRec.addActionListener(e -> exibirLista("Minhas Receitas", pacienteLogado.getReceitas().toArray()));
        
        JButton btnAtest = new JButton("Ver Atestados");
        btnAtest.addActionListener(e -> exibirLista("Meus Atestados", pacienteLogado.getAtestados().toArray()));

        JButton btnExame = new JButton("Ver Exames");
        btnExame.addActionListener(e -> exibirLista("Meus Exames", pacienteLogado.getExames().toArray()));

        painel.add(btnRec); painel.add(btnAtest); painel.add(btnExame);
        return painel;
    }

    // --- REQUISITO: Verificar Disponibilidade de Visitas ---
    private JPanel criarPainelConsultaVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JTextField txtBusca = new JTextField();
        JButton btnBuscar = new JButton("Consultar Status de Visita");
        JTextArea txtResultado = new JTextArea(5, 20);
        txtResultado.setEditable(false);

        btnBuscar.addActionListener(e -> {
            String nome = txtBusca.getText().toLowerCase();
            boolean achou = false;
            for (Paciente p : hospital.listarPacientes()) {
                if (p.getNome().toLowerCase().contains(nome) && p.getStatusInternacao() == StatusInternacao.INTERNADO) {
                    txtResultado.setText("Paciente: " + p.getNome() + "\nStatus: " + p.getStatusVisita().getDescricao());
                    achou = true;
                    break;
                }
            }
            if (!achou) txtResultado.setText("Paciente não encontrado ou não está internado.");
        });

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(new JLabel("Nome do Paciente Internado:"), BorderLayout.NORTH);
        pnlNorte.add(txtBusca, BorderLayout.CENTER);
        pnlNorte.add(btnBuscar, BorderLayout.SOUTH);

        painel.add(pnlNorte, BorderLayout.NORTH);
        painel.add(new JScrollPane(txtResultado), BorderLayout.CENTER);
        return painel;
    }

    private void abrirEdicaoDados() {
        // Requisito: Manter dados cadastrais atualizados
        String novoEmail = JOptionPane.showInputDialog(this, "Novo E-mail:", pacienteLogado.getEmail());
        if (novoEmail != null && !novoEmail.isEmpty()) {
            pacienteLogado.setEmail(novoEmail);
            JOptionPane.showMessageDialog(this, "Dados atualizados!");
        }
    }

    private void exibirLista(String titulo, Object[] dados) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(500, 400);
        JList<Object> lista = new JList<>(dados);
        JButton btnVer = new JButton("Visualizar Conteúdo");
        btnVer.addActionListener(e -> {
            if (lista.getSelectedValue() != null) 
                JOptionPane.showMessageDialog(dialog, lista.getSelectedValue().toString());
        });
        dialog.add(new JScrollPane(lista), BorderLayout.CENTER);
        dialog.add(btnVer, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}