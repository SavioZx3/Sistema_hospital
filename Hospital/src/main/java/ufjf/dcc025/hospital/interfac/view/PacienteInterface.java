package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;

public class PacienteInterface extends JFrame {
    public PacienteInterface() {
        setTitle("Paciente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        add(new JLabel("Área do Paciente", SwingConstants.CENTER));
        setVisible(true);
    }
}