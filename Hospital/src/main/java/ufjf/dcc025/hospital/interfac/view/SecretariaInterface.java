package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;

public class SecretariaInterface extends JFrame {
    public SecretariaInterface() {
        setTitle("Recepção");
        setSize(400, 300);
        setLocationRelativeTo(null);
        add(new JLabel("Área da Recepção", SwingConstants.CENTER));
        setVisible(true);
    }
}