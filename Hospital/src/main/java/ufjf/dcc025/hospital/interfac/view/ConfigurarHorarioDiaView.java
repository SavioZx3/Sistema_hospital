package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import ufjf.dcc025.hospital.usuarios.medico.DisponibilidadeDiaSemana;

public class ConfigurarHorarioDiaView extends JDialog {
    private JSpinner spinInicio, spinFim, spinDuracao;
    private DisponibilidadeDiaSemana resultado;
    private final DayOfWeek dia;

    public ConfigurarHorarioDiaView(Frame owner, DayOfWeek dia) {
        super(owner, "Configurar: " + dia.name(), true);
        this.dia = dia;
        setLayout(new GridLayout(4, 2, 10, 10));
        
        add(new JLabel("Hora Início:"));
        spinInicio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor edInicio = new JSpinner.DateEditor(spinInicio, "HH:mm");
        spinInicio.setEditor(edInicio);
        add(spinInicio);

        add(new JLabel("Hora Fim:"));
        spinFim = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor edFim = new JSpinner.DateEditor(spinFim, "HH:mm");
        spinFim.setEditor(edFim);
        add(spinFim);

        add(new JLabel("Duração (min):"));
        spinDuracao = new JSpinner(new SpinnerNumberModel(30, 10, 120, 5));
        add(spinDuracao);

        JButton btnSalvar = new JButton("Confirmar");
        btnSalvar.addActionListener(e -> salvar());
        add(btnSalvar);

        setSize(300, 250);
        setLocationRelativeTo(owner);
    }

    private void salvar() {
        try {
            LocalTime inicio = LocalTime.parse(new JSpinner.DateEditor(spinInicio, "HH:mm").getFormat().format(spinInicio.getValue()));
            LocalTime fim = LocalTime.parse(new JSpinner.DateEditor(spinFim, "HH:mm").getFormat().format(spinFim.getValue()));
            int min = (int) spinDuracao.getValue();
            
            this.resultado = new DisponibilidadeDiaSemana(dia, inicio, fim, Duration.ofMinutes(min));
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public DisponibilidadeDiaSemana getResultado() { return resultado; }
}