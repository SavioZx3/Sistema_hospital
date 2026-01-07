package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class GerenciamentoDadosView extends JPanel {

    public GerenciamentoDadosView() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Selecione um paciente ou médico", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}