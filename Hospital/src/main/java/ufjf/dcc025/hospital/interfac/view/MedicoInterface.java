package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;

public class MedicoInterface extends JFrame {
    public MedicoInterface() {
        setTitle("Médico");
        setSize(400, 300);
        setLocationRelativeTo(null);
        add(new JLabel("Área do Médico", SwingConstants.CENTER));
        setVisible(true);
    }
}