package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private JTable tabelaConsultas;

    public MedicoInterface(Medico medico, Hospital hospital) {
        this.medicoLogado = medico;
        this.hospital = hospital;

        setTitle("Painel do Médico - Dr(a). " + medicoLogado.getNome());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Sessão");
        JMenuItem itemSair = new JMenuItem("Sair (Voltar ao Login)");
        itemSair.addActionListener(e -> {
            dispose();
            new LoginInterface(hospital).setVisible(true);
        });
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minhas Consultas", criarPainelConsultas());
        abas.addTab("Minha Agenda (Configuração)", criarPainelConfiguracaoAgenda());
        abas.addTab("Visitas e Internações", criarPainelVisitas());

        add(abas);
        
        // Verifica notificações ao abrir
        SwingUtilities.invokeLater(this::verificarNotificacoes);
        
        setVisible(true);
    }

    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Paciente", "Data", "Horário", "Status", "Objeto"};
        modelConsultas = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaConsultas = new JTable(modelConsultas);
        tabelaConsultas.removeColumn(tabelaConsultas.getColumnModel().getColumn(4));

        JButton btnVerConsulta = new JButton("Ver Detalhes / Atender");
        btnVerConsulta.addActionListener(e -> abrirAtendimento());

        painel.add(new JLabel("Lista de Pacientes Agendados:"), BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);
        painel.add(btnVerConsulta, BorderLayout.SOUTH);

        atualizarTabelaConsultas();
        return painel;
    }

    private JPanel criarPainelConfiguracaoAgenda() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Definir Horários de Atendimento"));

        JCheckBox[] checksDias = {
            new JCheckBox("Segunda"), new JCheckBox("Terça"), new JCheckBox("Quarta"),
            new JCheckBox("Quinta"), new JCheckBox("Sexta"), new JCheckBox("Sábado")
        };
        java.time.DayOfWeek[] diasJava = {
            java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.TUESDAY, java.time.DayOfWeek.WEDNESDAY,
            java.time.DayOfWeek.THURSDAY, java.time.DayOfWeek.FRIDAY, java.time.DayOfWeek.SATURDAY
        };

        JPanel pnlChecks = new JPanel(new GridLayout(2, 3));
        for (JCheckBox cb : checksDias) pnlChecks.add(cb);

        JPanel pnlConfig = new JPanel(new FlowLayout());
        JComboBox<String> cbInicio = new JComboBox<>(new String[]{"08:00", "09:00", "13:00", "14:00"});
        JComboBox<String> cbFim = new JComboBox<>(new String[]{"12:00", "17:00", "18:00", "20:00"});
        JComboBox<Integer> cbDuracao = new JComboBox<>(new Integer[]{15, 20, 30, 60});

        pnlConfig.add(new JLabel("Início:")); pnlConfig.add(cbInicio);
        pnlConfig.add(new JLabel("Fim:")); pnlConfig.add(cbFim);
        pnlConfig.add(new JLabel("Duração (min):")); pnlConfig.add(cbDuracao);

        JButton btnGerar = new JButton("Gerar Horários na Agenda");
        btnGerar.addActionListener(e -> {
            try {
                List<DisponibilidadeDiaSemana> novaDisp = new ArrayList<>();
                for (int i = 0; i < checksDias.length; i++) {
                    if (checksDias[i].isSelected()) {
                        novaDisp.add(new DisponibilidadeDiaSemana(
                            diasJava[i], 
                            LocalTime.parse((String)cbInicio.getSelectedItem()), 
                            LocalTime.parse((String)cbFim.getSelectedItem()), 
                            java.time.Duration.ofMinutes((Integer)cbDuracao.getSelectedItem())
                        ));
                    }
                }
                if (novaDisp.isEmpty()) throw new Exception("Selecione ao menos um dia.");
                
                medicoLogado.atualizarDisponibilidadeSemanal(novaDisp);
                medicoLogado.getAgenda().gerarConsultasProximaSemana();
                atualizarTabelaConsultas();
                JOptionPane.showMessageDialog(this, "Horários gerados com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        painel.add(pnlChecks, BorderLayout.NORTH);
        painel.add(pnlConfig, BorderLayout.CENTER);
        painel.add(btnGerar, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelVisitas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        modelInternados = new DefaultTableModel(new String[]{"Paciente", "CPF", "Status Visita", "Objeto"}, 0);
        JTable tabelaInternados = new JTable(modelInternados);
        tabelaInternados.removeColumn(tabelaInternados.getColumnModel().getColumn(3));

        JPanel pnlAcoes = new JPanel(new FlowLayout());
        JButton btnAlta = new JButton("Dar Alta");
        JButton btnVisita = new JButton("Alternar Visita");

        btnAlta.addActionListener(e -> {
            int row = tabelaInternados.getSelectedRow();
            if (row != -1) {
                Paciente p = (Paciente) modelInternados.getValueAt(row, 3);
                p.setStatusInternacao(StatusInternacao.ALTA);
                atualizarTabelaInternados();
            }
        });

        btnVisita.addActionListener(e -> {
            int row = tabelaInternados.getSelectedRow();
            if (row != -1) {
                Paciente p = (Paciente) modelInternados.getValueAt(row, 3);
                p.setStatusVisita(p.getStatusVisita() == StatusVisita.APTO ? StatusVisita.INAPTO : StatusVisita.APTO);
                atualizarTabelaInternados();
            }
        });

        pnlAcoes.add(btnVisita); pnlAcoes.add(btnAlta);
        painel.add(new JScrollPane(tabelaInternados), BorderLayout.CENTER);
        painel.add(pnlAcoes, BorderLayout.SOUTH);
        atualizarTabelaInternados();
        return painel;
    }

    private void abrirAtendimento() {
        int row = tabelaConsultas.getSelectedRow();
        if (row == -1) return;
        Consulta c = (Consulta) modelConsultas.getValueAt(row, 4);
        if (c.getPaciente() == null) return;

        JDialog telaAtendimento = new JDialog(this, "Atendimento: " + c.getPaciente().getNome(), true);
        telaAtendimento.setSize(500, 400);
        telaAtendimento.setLayout(new BorderLayout(5, 5));
        
        JTextArea areaTexto = new JTextArea();
        areaTexto.setBorder(BorderFactory.createTitledBorder("Evolução Clínica / Prescrição"));
        
        JButton btnFinalizar = new JButton("Finalizar e Concluir Consulta");
        btnFinalizar.addActionListener(e -> {
            try {
                c.marcarInicioConsulta(); // Garante o fluxo do StatusConsulta
                c.marcarConcluida();
                telaAtendimento.dispose();
                atualizarTabelaConsultas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaAtendimento, ex.getMessage());
            }
        });

        telaAtendimento.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        telaAtendimento.add(btnFinalizar, BorderLayout.SOUTH);
        telaAtendimento.setLocationRelativeTo(this);
        telaAtendimento.setVisible(true);
    }

    private void atualizarTabelaConsultas() {
        modelConsultas.setRowCount(0);
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        for (Consulta c : medicoLogado.getAgenda().getConsultas()) {
            modelConsultas.addRow(new Object[]{
                (c.getPaciente() != null ? c.getPaciente().getNome() : "---"),
                c.getData(), c.getDataHoraInicio().format(hm), c.getStatus(), c
            });
        }
    }

    private void atualizarTabelaInternados() {
        modelInternados.setRowCount(0);
        for (Paciente p : hospital.listarPacientes()) {
            if (p.getStatusInternacao() == StatusInternacao.INTERNADO) {
                modelInternados.addRow(new Object[]{p.getNome(), p.getCpf(), p.getStatusVisita(), p});
            }
        }
    }

    private void verificarNotificacoes() {
        List<String> avisos = medicoLogado.getAgenda().consumirNotificacoes();
        if (!avisos.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", avisos), "Avisos da Agenda", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}