package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class MedicoInterface extends JFrame {
    private final Medico medicoLogado;
    private final Hospital hospital;
    private JTable tabelaConsultas;
    private DefaultTableModel model;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;

        setTitle("Consultório - Dr(a). " + medicoLogado.getNome());
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TABELA DE CONSULTAS ---
        String[] colunas = {"Horário", "Paciente", "Status", "Objeto"};
        model = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaConsultas = new JTable(model);
        // Oculta a coluna que guarda o objeto Consulta para o usuário não ver
        tabelaConsultas.removeColumn(tabelaConsultas.getColumnModel().getColumn(3)); 
        
        atualizarTabela();

        // --- PAINEL DE AÇÕES ---
        JPanel pnlAcoes = new JPanel(new GridLayout(1, 4, 10, 10));
        pnlAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnIniciar = new JButton("Iniciar Consulta");
        JButton btnReceita = new JButton("Emitir Receita");
        JButton btnAtestado = new JButton("Emitir Atestado");
        JButton btnFinalizar = new JButton("Finalizar");

        // Botão de Sair/Logout
        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            dispose();
            new LoginInterface(hospital).setVisible(true);
        });

        btnIniciar.addActionListener(e -> gerenciarStatus(1));
        btnReceita.addActionListener(e -> abrirDialogoReceita());
        btnAtestado.addActionListener(e -> abrirDialogoAtestado());
        btnFinalizar.addActionListener(e -> gerenciarStatus(2));

        pnlAcoes.add(btnIniciar);
        pnlAcoes.add(btnReceita);
        pnlAcoes.add(btnAtestado);
        pnlAcoes.add(btnFinalizar);

        add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);
        add(pnlAcoes, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private void atualizarTabela() {
        model.setRowCount(0);
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        if (medicoLogado.getAgenda() != null) {
            for (Consulta c : medicoLogado.getAgenda().getConsultas()) {
                model.addRow(new Object[]{
                    c.getDataHoraInicio().format(hm),
                    (c.getPaciente() != null ? c.getPaciente().getNome() : "---"),
                    c.getStatus(),
                    c
                });
            }
        }
    }

    private Consulta getConsultaSelecionada() {
        int row = tabelaConsultas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela.");
            return null;
        }
        return (Consulta) model.getValueAt(row, 3);
    }

    private void gerenciarStatus(int acao) {
        Consulta c = getConsultaSelecionada();
        if (c == null) return;
        try {
            if (acao == 1) c.marcarInicioConsulta();
            else c.marcarConcluida();
            atualizarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void abrirDialogoReceita() {
        Consulta c = getConsultaSelecionada();
        if (c == null || c.getPaciente() == null) return;

        Paciente p = c.getPaciente();
        JTextField txtMedicamento = new JTextField();
        JTextField txtQtd = new JTextField();
        Object[] message = {
            "Paciente: " + p.getNome(),
            "Medicamento:", txtMedicamento,
            "Quantidade/Instruções:", txtQtd,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Nova Receita", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                c.emitirReceita(txtMedicamento.getText(), txtQtd.getText(), LocalDate.now().plusMonths(1));
                
                // INTEGRANDO COM O PACIENTE:
                p.adicionarReceita(c.getReceita()); 
                
                JOptionPane.showMessageDialog(this, "Receita emitida e salva no histórico do paciente!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void abrirDialogoAtestado() {
        Consulta c = getConsultaSelecionada();
        if (c == null || c.getPaciente() == null) return;

        Paciente p = c.getPaciente();
        String diasStr = JOptionPane.showInputDialog(this, "Duração do atestado para " + p.getNome() + " (dias):");
        
        if (diasStr != null && !diasStr.isEmpty()) {
            try {
                int dias = Integer.parseInt(diasStr);
                c.emitirAtestado(dias);
                
                // INTEGRANDO COM O PACIENTE:
                p.adicionarAtestado(c.getAtestado());
                
                JOptionPane.showMessageDialog(this, "Atestado de " + dias + " dias registrado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: Insira um número válido.");
            }
        }
    }
}