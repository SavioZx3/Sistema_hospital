package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class DadosPacienteView extends JPanel {

    public DadosPacienteView() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Dados pessoais", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        JPanel dados = new JPanel(new GridLayout(5, 2, 10, 10));
        dados.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        dados.add(new JLabel("Nome:"));
        dados.add(new JTextField("Paciente Exemplo"));

        dados.add(new JLabel("CPF:"));
        dados.add(new JTextField("000.000.000-00"));

        dados.add(new JLabel("Endereço:"));
        dados.add(new JTextField("Rua X"));

        dados.add(new JLabel("Email:"));
        dados.add(new JTextField("email@email.com"));

        dados.add(new JLabel("Senha:"));
        dados.add(new JPasswordField("123456"));

        add(dados, BorderLayout.CENTER);
    }
}