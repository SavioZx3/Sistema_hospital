package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;
import ufjf.dcc025.hospital.usuarios.paciente.StatusVisita;

public class MedicoInterface extends JFrame {

    private final Medico medicoLogado;
    private final Hospital hospital;
    private DefaultTableModel modelConsultas;
    private DefaultTableModel modelInternados;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;

        setTitle("Painel Médico - Dr(a). " + medicoLogado.getNome());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarMenu();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab(" Atendimentos", criarPainelConsultas());
        abas.addTab(" Configurar Agenda", criarPainelConfiguracaoAgenda());
        abas.addTab(" Internações", criarPainelVisitas());

        add(abas);
        verificarAvisos();
    }

    private void configurarMenu() {
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("Sessão");
        JMenuItem sair = new JMenuItem("Sair (Logout)");
        sair.addActionListener(e -> { dispose(); new LoginInterface(hospital).setVisible(true); });
        m.add(sair);
        mb.add(m);
        setJMenuBar(mb);
    }

    private JPanel criarPainelConsultas() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelConsultas = new DefaultTableModel(new String[]{"Paciente", "Data", "Hora", "Status", "OBJ"}, 0);
        JTable t = new JTable(modelConsultas);
        t.removeColumn(t.getColumnModel().getColumn(4));

        JButton btnAtender = new JButton(" Iniciar Atendimento");
        btnAtender.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row != -1) atender((Consulta) modelConsultas.getValueAt(row, 4));
        });

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(btnAtender, BorderLayout.SOUTH);
        atualizarConsultas();
        return p;
    }

    private void atender(Consulta c) {
        if (c.getPaciente() == null) return;
        String evolucao = JOptionPane.showInputDialog(this, "Evolução Clínica do Paciente " + c.getPaciente().getNome() + ":");
        if (evolucao != null) {
            try {
                c.marcarInicioConsulta();
                c.marcarConcluida();
                atualizarConsultas();
                JOptionPane.showMessageDialog(this, "Consulta Finalizada!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    }

    private JPanel criarPainelConfiguracaoAgenda() {
        JPanel p = new JPanel(new GridLayout(4, 1, 10, 10));
        p.setBorder(BorderFactory.createTitledBorder("Gerar Horários"));

        JCheckBox[] dias = {new JCheckBox("Seg"), new JCheckBox("Ter"), new JCheckBox("Qua"), new JCheckBox("Qui"), new JCheckBox("Sex")};
        JPanel pnlDias = new JPanel();
        for(JCheckBox cb : dias) pnlDias.add(cb);

        JComboBox<String> horaIni = new JComboBox<>(new String[]{"08:00", "09:00", "13:00"});
        JComboBox<Integer> duracao = new JComboBox<>(new Integer[]{20, 30, 60});

        JButton btnGerar = new JButton("Gerar Slots da Semana");
        btnGerar.addActionListener(e -> {
            List<DisponibilidadeDiaSemana> lista = new ArrayList<>();
            // Lógica simplificada de geração para demonstração
            medicoLogado.getAgenda().gerarConsultasProximaSemana(); 
            atualizarConsultas();
            JOptionPane.showMessageDialog(this, "Agenda Atualizada!");
        });

        p.add(pnlDias); p.add(new JLabel("Horário Início e Duração (min):"));
        JPanel pnlSel = new JPanel(); pnlSel.add(horaIni); pnlSel.add(duracao);
        p.add(pnlSel); p.add(btnGerar);
        return p;
    }

    private JPanel criarPainelVisitas() {
        JPanel p = new JPanel(new BorderLayout());
        modelInternados = new DefaultTableModel(new String[]{"Paciente", "CPF", "Visita", "OBJ"}, 0);
        JTable t = new JTable(modelInternados);
        t.removeColumn(t.getColumnModel().getColumn(3));

        JButton btnAlternar = new JButton(" Alternar Status Visita (Apto/Inapto)");
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

    private void atualizarConsultas() {
        modelConsultas.setRowCount(0);
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        medicoLogado.getAgenda().getConsultas().forEach(c -> 
            modelConsultas.addRow(new Object[]{c.getPaciente() != null ? c.getPaciente().getNome() : "---", c.getData(), c.getDataHoraInicio().format(hm), c.getStatus(), c}));
    }

    private void atualizarInternados() {
        modelInternados.setRowCount(0);
        hospital.listarPacientes().stream()
            .filter(pa -> pa.getStatusInternacao() == StatusInternacao.INTERNADO)
            .forEach(pa -> modelInternados.addRow(new Object[]{pa.getNome(), pa.getCpf(), pa.getStatusVisita(), pa}));
    }

    private void verificarAvisos() {
        List<String> avisos = medicoLogado.getAgenda().consumirNotificacoes();
        if(!avisos.isEmpty()) JOptionPane.showMessageDialog(this, String.join("\n", avisos));
    }
}