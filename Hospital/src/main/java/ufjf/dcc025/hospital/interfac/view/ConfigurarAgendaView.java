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
    private final List<DisponibilidadeDiaSemana> disponibilidades =
            new ArrayList<>();

    public ConfigurarAgendaView(Frame owner, Medico medico) {
        super(owner, "Configurar Agenda Semanal", true);
        this.medico = medico;

        setLayout(new BorderLayout(15, 15));

        JLabel lblInfo = new JLabel(
                "<html><div style='text-align:center;padding:10px;'>"
                        + "Clique nos dias para configurar os horários."
                        + "</div></html>"
        );

        add(lblInfo, BorderLayout.NORTH);
        criarBotoesDias();
        criarBotaoFinalizar();

        setSize(400, 600);
        setLocationRelativeTo(owner);
    }

    private void criarBotoesDias() {
        JPanel painel = new JPanel(new GridLayout(7, 1, 5, 5));

        for (DayOfWeek dia : DayOfWeek.values()) {
            String nome = dia.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    new java.util.Locale("pt", "BR")
            ).toUpperCase();

            JButton btn = new JButton(nome);
            btn.addActionListener(e -> configurarDia(dia, btn, nome));
            painel.add(btn);
        }

        add(new JScrollPane(painel), BorderLayout.CENTER);
    }

    private void configurarDia(
            DayOfWeek dia, JButton btn, String nome) {

        ConfigurarHorarioDiaView view =
                new ConfigurarHorarioDiaView(
                        (Frame) getOwner(), dia);

        view.setVisible(true);

        if (view.getResultado() != null) {
            disponibilidades.removeIf(d ->
                    d.getDiaSemana().equals(dia));

            disponibilidades.add(view.getResultado());

            btn.setText(nome + " ✔");
            btn.setEnabled(false);
            btn.setBackground(new Color(46, 204, 113));
            btn.setForeground(Color.WHITE);
        }
    }

    private void criarBotaoFinalizar() {
        JButton btn = new JButton("GERAR AGENDA");
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));

        btn.addActionListener(e -> finalizar());
        add(btn, BorderLayout.SOUTH);
    }

    private void finalizar() {
        if (disponibilidades.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Configure ao menos um dia.");
            return;
        }

        medico.atualizarDisponibilidadeSemanal(disponibilidades);
        medico.getAgenda().carregarNovaGrade(disponibilidades);

        JOptionPane.showMessageDialog(this,
                "Agenda gerada com sucesso!");
        dispose();
    }
}
