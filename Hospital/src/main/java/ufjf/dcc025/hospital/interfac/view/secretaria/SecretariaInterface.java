package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class SecretariaInterface extends BaseView {

    private DefaultTableModel modelConsultas;
    private DefaultTableModel modelPacientes;
    private DefaultTableModel modelMedicos;

    public SecretariaInterface(Hospital hospital, Recepcionista r) {
        super(hospital, "Recepção - " + r.getNome());

        add(criarTopo("Recepcionista: " + r.getNome()), BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Consultas", criarPainelConsultas());
        abas.addTab("Pacientes", criarPainelPacientes());
        abas.addTab("Médicos", criarPainelMedicos());

        add(abas, BorderLayout.CENTER);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel criarPainelConsultas() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        modelConsultas = new DefaultTableModel(new String[]{"Médico", "Paciente", "Data", "Status", "OBJ"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        JTable tabela = new JTable(modelConsultas);
        tabela.removeColumn(tabela.getColumnModel().getColumn(4));

        JButton btnMarcar = new JButton("Marcar Consulta");
        JButton btnCancelar = new JButton("Desmarcar Consulta");

        btnMarcar.addActionListener(e -> abrirMarcacaoConsulta());
        btnCancelar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row == -1) { mostrarAviso("Selecione uma consulta."); return; }

            Consulta c = (Consulta) modelConsultas.getValueAt(row, 4);
            if (c.getPaciente() == null) { mostrarAviso("Esta consulta não está marcada."); return; }

            try {
                hospital.cancelarAgendamento(c);
                hospital.salvarAlteracoes(); // Persistência
                mostrarInfo("Consulta desmarcada com sucesso.");
                c.getMedico().getAgenda().notificarCancelamento();
                atualizarConsultas();
            } catch (Exception ex) {
                mostrarErro(ex);
            }
        });

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnMarcar); botoes.add(btnCancelar);

        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        p.add(botoes, BorderLayout.SOUTH);
        atualizarConsultas();
        return p;
    }

    private void abrirMarcacaoConsulta() {
        new AgendaConsultasView(hospital, this::atualizarConsultas).setVisible(true);
    }

private void atualizarConsultas() {
    modelConsultas.setRowCount(0);

    for (Consulta c : hospital.getConsultasGerais()) {

        // 1. Médico ativo
        if (!c.getMedico().isAtivo()) {
            continue;
        }

        // 2. Status permitido
        if (c.getStatus() != StatusConsulta.AGENDADA &&
            c.getStatus() != StatusConsulta.DISPONIVEL) {
            continue;
        }

        modelConsultas.addRow(new Object[]{
            c.getMedico().getNome(),
            c.getPaciente() != null ? c.getPaciente().getNome() : "Livre",
            c.getData(),
            c.getStatus(),
            c
        });
    }
}

    private JPanel criarPainelPacientes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        modelPacientes = new DefaultTableModel(new String[]{"Nome", "CPF", "Internação"}, 0);
        JTable t = new JTable(modelPacientes);

        JButton btnNovoPaciente = new JButton("Cadastrar Paciente");
        btnNovoPaciente.addActionListener(e -> new CadastroPacienteView(hospital, this::atualizarPacientes).setVisible(true));

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnNovoPaciente, BorderLayout.SOUTH);
        atualizarPacientes();
        return p;
    }

    private void atualizarPacientes() {
        modelPacientes.setRowCount(0);
        for (Paciente p : hospital.listarPacientes()) {
            modelPacientes.addRow(new Object[]{p.getNome(), p.getCpf(), p.getStatusInternacao()});
        }
    }

    private JPanel criarPainelMedicos() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        modelMedicos = new DefaultTableModel(new String[]{"Nome", "Especialidade", "Ativo", "OBJ"}, 0);
        JTable t = new JTable(modelMedicos);
        t.removeColumn(t.getColumnModel().getColumn(3));

        JButton btnToggle = new JButton("Ativar / Desativar");
        btnToggle.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row == -1) { mostrarAviso("Selecione um médico."); return; }
            Medico m = (Medico) modelMedicos.getValueAt(row, 3);
            if (m.isAtivo()) {
                if(m.getAgenda().getConsultasMarcadas() == null || !m.getAgenda().getConsultasMarcadas().isEmpty()) 
                    mostrarAviso("Não é possível desativar um médico com consultas futuras agendadas.");
                else
                    m.desativar();
            } else {
                m.ativar();
            }
            atualizarConsultas();
            atualizarMedicos();
        });

        JButton btnNovoMedico = new JButton("Cadastrar Médico");
        btnNovoMedico.addActionListener(e -> new CadastroMedicoInterface(hospital, this::atualizarMedicos).setVisible(true));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnNovoMedico); botoes.add(btnToggle);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(botoes, BorderLayout.SOUTH);
        atualizarMedicos();
        return p;
    }

    private void atualizarMedicos() {
        modelMedicos.setRowCount(0);
        for (Medico m : hospital.listarMedicos()) {
            modelMedicos.addRow(new Object[]{m.getNome(), m.getEspecialidade(), m.isAtivo() ? "Ativo" : "Inativo", m});
        }
    }
}