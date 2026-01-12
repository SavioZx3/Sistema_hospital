package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class SecretariaInterface extends JFrame {

    private final Hospital hospital;
    private DefaultTableModel modelConsultas, modelPacientes;

    public SecretariaInterface(Recepcionista secretaria, Hospital hospital) {
        this.hospital = hospital;
        configurarJanela();

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setBackground(new Color(44, 62, 80));
        painelSuperior.setPreferredSize(new Dimension(1100, 50));
        
        JLabel lbl = new JLabel("  RECEPCIONISTA: " + secretaria.getNome().toUpperCase());
        lbl.setForeground(Color.WHITE);
        
        JButton btnLogout = new JButton("Sair");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> executarSair());
        
        painelSuperior.add(lbl, BorderLayout.WEST);
        painelSuperior.add(btnLogout, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Agenda Geral", criarPainelAgenda());
        abas.addTab("Pacientes", criarPainelPacientes());
        abas.addTab("Gestão de Médicos", criarPainelMedicos());

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Administração Hospitalar");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private JPanel criarPainelAgenda() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        modelConsultas = new DefaultTableModel(new String[]{"Médico", "Paciente", "Data", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelConsultas);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnDesmarcar = new JButton(" DESMARCAR CONSULTA SELECIONADA");
        btnDesmarcar.setBackground(new Color(231, 76, 60));
        btnDesmarcar.setForeground(Color.WHITE);
        btnDesmarcar.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                Consulta c = (Consulta) modelConsultas.getValueAt(row, 4);
                if (c.getStatus() == StatusConsulta.AGENDADA) {
                    c.cancelarConsulta(); // Libera médico e paciente
                    atualizarTabelaConsultas();
                    JOptionPane.showMessageDialog(this, "Consulta cancelada.");
                }
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnDesmarcar, BorderLayout.SOUTH);
        atualizarTabelaConsultas();
        return p;
    }

    private JPanel criarPainelMedicos() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel modelM = new DefaultTableModel(new String[]{"Médico", "Especialidade", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelM);
        t.removeColumn(t.getColumnModel().getColumn(3));

        JButton btnStatus = new JButton("Ativar/Desativar Médico");
        btnStatus.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) {
                Medico m = (Medico) modelM.getValueAt(row, 3);
                if (m.isAtivo()) m.desativar(); else m.ativar();
                // Lógica de refresh da tabela de médicos aqui
            }
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnStatus, BorderLayout.SOUTH);
        return p;
    }

    private JPanel criarPainelPacientes() {
        JPanel p = new JPanel(new BorderLayout());
        modelPacientes = new DefaultTableModel(new String[]{"Nome", "CPF", "Internação"}, 0);
        p.add(new JScrollPane(new JTable(modelPacientes)), BorderLayout.CENTER);
        atualizarListaPacientes();
        return p;
    }

    public void atualizarTabelaConsultas() {
        modelConsultas.setRowCount(0);
        for (Consulta c : hospital.getConsultasGerais()) {
            modelConsultas.addRow(new Object[]{
                c.getMedico().getNome(), 
                (c.getPaciente() != null ? c.getPaciente().getNome() : "LIVRE"), 
                c.getData(), c.getStatus(), c
            });
        }
    }

    private void atualizarListaPacientes() {
        modelPacientes.setRowCount(0);
        for (Paciente p : hospital.listarPacientes()) {
            modelPacientes.addRow(new Object[]{p.getNome(), p.getCpf(), p.getStatusInternacao()});
        }
    }

    private void executarSair() { dispose(); new LoginInterface(hospital); }
}