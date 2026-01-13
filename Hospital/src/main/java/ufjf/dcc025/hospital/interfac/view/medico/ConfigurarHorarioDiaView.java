package ufjf.dcc025.hospital.interfac.view.medico;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;


public class ConfigurarHorarioDiaView extends JDialog {

    private DisponibilidadeDiaSemana resultado;
    private final DayOfWeek dia;

    private JSpinner spInicio;
    private JSpinner spFim;
    private JSpinner spDuracao;

    public ConfigurarHorarioDiaView(Frame owner, DayOfWeek dia) {
        super(owner, "Configurar " + dia.name(), true);
        this.dia = dia;

        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(300, 220);
        setLocationRelativeTo(owner);

        add(new JLabel("Hora início:"));
        spInicio = criarSpinnerHora();
        add(spInicio);

        add(new JLabel("Hora fim:"));
        spFim = criarSpinnerHora();
        add(spFim);

        add(new JLabel("Duração (min):"));
        spDuracao = new JSpinner(new SpinnerNumberModel(30, 10, 120, 5));
        add(spDuracao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        add(btnSalvar);
    }

    private JSpinner criarSpinnerHora() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        return spinner;
    }

    private void salvar() {
        try {
            LocalTime inicio = LocalTime.parse(
                    new JSpinner.DateEditor(spInicio, "HH:mm")
                            .getFormat().format(spInicio.getValue()));

            LocalTime fim = LocalTime.parse(
                    new JSpinner.DateEditor(spFim, "HH:mm")
                            .getFormat().format(spFim.getValue()));

            int duracao = (int) spDuracao.getValue();

            resultado = new DisponibilidadeDiaSemana(
                    dia, inicio, fim, Duration.ofMinutes(duracao));

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao configurar horário: " + ex.getMessage());
        }
    }

    public DisponibilidadeDiaSemana getResultado() {
        return resultado;
    }
}
