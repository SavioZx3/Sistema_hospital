package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import ufjf.dcc025.hospital.interfac.controller.LoginController;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class LoginInterface extends JFrame {

    private JTextField email;
    private JPasswordField senha;
    private LoginController controller = new LoginController();

    public LoginInterface() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        email = new JTextField();
        senha = new JPasswordField();

        JButton entrar = new JButton("Entrar");
        entrar.addActionListener(e -> autenticar());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Email"));
        add(email);
        add(new JLabel("Senha"));
        add(senha);
        add(entrar);

        setVisible(true);
    }

    private void autenticar() {
        try {
            Usuario u = controller.autenticar(
                email.getText(),
                new String(senha.getPassword())
            );

            dispose();

            switch (u.getTipo()) {
                case "MEDICO" -> new MedicoInterface(u);
                case "PACIENTE" -> new PacienteInterface(u);
                case "RECEPCIONISTA" -> new SecretariaInterface(u);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}