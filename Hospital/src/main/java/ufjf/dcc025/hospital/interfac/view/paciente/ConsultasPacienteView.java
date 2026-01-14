package ufjf.dcc025.hospital.interfac.view.paciente;
/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class ConsultasPacienteView extends BaseView {

    private Paciente pacienteLogado;
    private JTable tabela;
    private DefaultTableModel model;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ConsultasPacienteView(Paciente paciente, Hospital hospital) {
        
        super(hospital, "Meu Histórico de Consultas - " + paciente.getNome());
        this.pacienteLogado = paciente;

        setTitle("Meu Histórico de Consultas - " + pacienteLogado.getNome());
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        criarTabela();
        popularDados();

        // Painel inferior com botão de ação
        JPanel painelAcoes = new JPanel();
        JButton btnVerDetalhes = new JButton(" Ver Detalhes da Consulta");
        btnVerDetalhes.addActionListener(e -> mostrarDetalhesConsulta());
        painelAcoes.add(btnVerDetalhes);
        add(painelAcoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void criarTabela() {
        String[] colunas = {"Data/Hora", "Médico", "Status", "Documentos"};
        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void popularDados() {
        model.setRowCount(0);
        for (Consulta c : hospital.getConsultasGerais()) {
            if (c.getPaciente() != null && c.getPaciente().getCpf().equals(pacienteLogado.getCpf())) {
                
                StringBuilder docs = new StringBuilder();
                if (c.getAtestado() != null) docs.append("Atestado ");
                if (c.getReceita() != null) docs.append("Receita ");
                if (c.getExame() != null) docs.append("Exame ");
                
                Object[] linha = {
                    c.getDataHoraInicio().format(dtf),
                    "Dr(a). " + c.getMedico().getNome(),
                    c.getStatus(),
                    docs.length() == 0 ? "---" : docs.toString(),
                    c
                };
                model.addRow(linha);
            }
        }
    }

    private void mostrarDetalhesConsulta() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            mostrarAviso("Selecione uma consulta na tabela.");
            return;
        }

        // Recupera a consulta correta
        // Nota: Idealmente você teria um ID, aqui buscamos pela lógica da lista
        Consulta consulta = null;
        int count = 0;
        for (Consulta c : hospital.getConsultasGerais()) {
            if (c.getPaciente() != null && c.getPaciente().getCpf().equals(pacienteLogado.getCpf())) {
                if (count == linhaSelecionada) {
                    consulta = c;
                    break;
                }
                count++;
            }
        }

        if (consulta != null) {
            StringBuilder info = new StringBuilder();
            info.append("=== Detalhes do Atendimento ===\n");
            info.append("Médico: ").append(consulta.getMedico().getNome()).append("\n");
            info.append("Status: ").append(consulta.getStatus()).append("\n\n");
            
            if (consulta.getReceita() != null) {
                info.append("--- RECEITA ---\n").append(consulta.getReceita().toString()).append("\n\n");
            }
            if (consulta.getAtestado() != null) {
                info.append("--- ATESTADO ---\n").append(consulta.getAtestado().toString()).append("\n\n");
            }

            JTextArea area = new JTextArea(info.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scroll, "Prontuário da Consulta", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
