package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;

public class ConfigurarAgendaView extends JDialog {
    private final Medico medico;
    private final List<DisponibilidadeDiaSemana> disponibilidades = new ArrayList<>();
    private JPanel pnlBotoesDias;

    public ConfigurarAgendaView(Frame owner, Medico medico) {
        super(owner, "Configurar Agenda Semanal", true);
        this.medico = medico;
        
        setLayout(new BorderLayout(15, 15));
        
        JLabel lblInfo = new JLabel("<html><div style='text-align: center; padding: 10px;'>"
                + "Clique nos dias em que você realizará atendimentos para configurar os horários.</div></html>");
        add(lblInfo, BorderLayout.NORTH);

        pnlBotoesDias = new JPanel(new GridLayout(7, 1, 5, 5));
        for (DayOfWeek dia : DayOfWeek.values()) {
            JButton btnDia = new JButton(dia.name());
            btnDia.addActionListener(e -> {
                ConfigurarHorarioDiaView config = new ConfigurarHorarioDiaView(owner, dia);
                config.setVisible(true);
                if (config.getResultado() != null) {
                    disponibilidades.add(config.getResultado());
                    btnDia.setBackground(new Color(46, 204, 113));
                    btnDia.setText(dia.name() + " - CONFIGURADO");
                }
            });
            pnlBotoesDias.add(btnDia);
        }
        
        add(new JScrollPane(pnlBotoesDias), BorderLayout.CENTER);

        JButton btnFinalizar = new JButton(" GERAR AGENDA NO SISTEMA");
        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(41, 128, 185));
        btnFinalizar.setForeground(Color.WHITE);
        
        btnFinalizar.addActionListener(e -> {
            if (disponibilidades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Configure ao menos um dia!");
                return;
            }

            try {
                // 1. Usa o método que JÁ EXISTE da classe Medico
                medico.atualizarDisponibilidadeSemanal(disponibilidades); 

                // 2. Gera os horários na agenda
                medico.getAgenda().carregarNovaGrade(disponibilidades);

                JOptionPane.showMessageDialog(this, "Agenda configurada e gerada com sucesso!");
                dispose();
            } catch (IllegalArgumentException ex) {
                // Isso vai capturar as validações (como a de carga horária mínima de 8h)
                JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage());
            }
        });

        add(btnFinalizar, BorderLayout.SOUTH);
        setSize(400, 600);
        setLocationRelativeTo(owner);
    }
}