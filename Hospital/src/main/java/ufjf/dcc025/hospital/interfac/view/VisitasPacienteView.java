package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class VisitasPacienteView extends JPanel {

    public VisitasPacienteView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titulo = new JLabel("Área de visitas");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField campoBusca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar paciente");

        JLabel resultado = new JLabel("Paciente apto à visita");

        gbc.gridy = 0;
        add(titulo, gbc);

        gbc.gridy = 1;
        add(campoBusca, gbc);

        gbc.gridy = 2;
        add(btnBuscar, gbc);

        gbc.gridy = 3;
        add(resultado, gbc);
    }
}