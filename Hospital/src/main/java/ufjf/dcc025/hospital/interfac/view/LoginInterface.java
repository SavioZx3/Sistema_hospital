package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import ufjf.dcc025.hospital.interfac.controller.LoginController;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.Recepcionista;

public class LoginInterface extends JFrame {

    private JTextField email;
    private JPasswordField senha;
    private LoginController controller = new LoginController();

    public LoginInterface() {
        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        email = new JTextField(20);
        senha = new JPasswordField(20);
        JButton entrar = new JButton("Entrar");

        entrar.addActionListener(e -> autenticar());

        JPanel p = new JPanel();
        p.add(new JLabel("Email:"));
        p.add(email);
        p.add(new JLabel("Senha:"));
        p.add(senha);
        p.add(entrar);

        add(p);
        setVisible(true);
    }

    private void autenticar() {
        try {
            Usuario u = controller.autenticar(
                email.getText(),
                new String(senha.getPassword())
            );

            dispose();

            if (u instanceof Paciente) new PacienteInterface();
            else if (u instanceof Medico) new MedicoInterface();
            else if (u instanceof Recepcionista) new SecretariaInterface();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}