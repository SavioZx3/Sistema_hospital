package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.controller.LoginController;
import ufjf.dcc025.hospital.interfac.view.medico.MedicoInterface;
import ufjf.dcc025.hospital.interfac.view.paciente.PacienteInterface;
import ufjf.dcc025.hospital.interfac.view.secretaria.SecretariaInterface;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.Recepcionista;

public class LoginInterface extends BaseView {

    private JTextField email;
    private JPasswordField senha;
    private LoginController controller;

    public LoginInterface(Hospital hospital) {
        super(hospital, "Hospital - Acesso ao Sistema");
        this.controller = new LoginController(hospital);

        
        for (java.awt.event.WindowListener wl : getWindowListeners()) {
            removeWindowListener(wl);
        }

        configurarJanela();
        criarComponentes();
        setVisible(true);
    }

    private void configurarJanela() {
        setSize(400, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {
        JPanel topo = new JPanel();
        topo.setBackground(new Color(41, 128, 185));
        JLabel lbl = new JLabel("LOGIN");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        topo.add(lbl);
        add(topo, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(2, 2, 10, 25));
        campos.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

        campos.add(new JLabel("E-mail:"));
        email = new JTextField();
        campos.add(email);

        campos.add(new JLabel("Senha:"));
        senha = new JPasswordField();
        // Permite dar 'Enter' no campo de senha para logar
        senha.addActionListener(e -> autenticar());
        campos.add(senha);

        JButton entrar = new JButton("Entrar no Sistema");
        entrar.setPreferredSize(new Dimension(0, 40));
        entrar.addActionListener(e -> autenticar());

        add(campos, BorderLayout.CENTER);
        add(entrar, BorderLayout.SOUTH);
    }

    private void autenticar() {
        try {
            // Se o login falhar, o controller deve lançar uma Exception
            Usuario u = controller.autenticar(
                    email.getText(),
                    new String(senha.getPassword())
            );

            // Se chegou aqui, login deu certo
            dispose(); 

            if (u instanceof Paciente p) {
                new PacienteInterface(hospital, p);
            } else if (u instanceof Medico m) {
                new MedicoInterface(hospital, m);
            } else if (u instanceof Recepcionista r) {
                new SecretariaInterface(hospital, r);
            }

        } catch (Exception e) {
            mostrarErro("Acesso negado: " + e.getMessage());
            senha.setText("");
            senha.requestFocus();
        }
    }
}