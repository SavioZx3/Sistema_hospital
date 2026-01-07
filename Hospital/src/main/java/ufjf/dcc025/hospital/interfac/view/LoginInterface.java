package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.interfac.controller.LoginController;

/**
 * Interface gráfica de login do sistema.
 * Responsável apenas por autenticar e redirecionar
 * o usuário para a interface correta.
 */
public class LoginInterface extends JFrame {

    private JTextField campoEmail;
    private LoginController controller;

    public LoginInterface() {
        controller = new LoginController();

        setTitle("Sistema Hospitalar - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarFormulario();
        criarBotoes();

        setVisible(true);
    }

    private void criarFormulario() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        panel.add(campoEmail);

        add(panel, BorderLayout.CENTER);
    }

    private void criarBotoes() {
        JPanel botoes = new JPanel();

        JButton btnEntrar = new JButton("Entrar");
        JButton btnSair = new JButton("Sair");

        btnEntrar.addActionListener(e -> autenticar());
        btnSair.addActionListener(e -> System.exit(0));

        botoes.add(btnEntrar);
        botoes.add(btnSair);

        add(botoes, BorderLayout.SOUTH);
    }

    private void autenticar() {
        String email = campoEmail.getText();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o email.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        LoginController.TipoUsuario tipo = controller.autenticar(email);

        dispose(); // fecha login

        switch (tipo) {
            case MEDICO -> new MedicoInterface();
            case PACIENTE -> new PacienteInterface();
            case RECEPCIONISTA -> new SecretariaInterface();
        }
    }
}