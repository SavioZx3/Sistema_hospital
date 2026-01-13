package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.controller.LoginController;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.Recepcionista;

public class LoginInterface extends JFrame {

    private JTextField email;
    private JPasswordField senha;
    private LoginController controller;
    private Hospital hospital;

    public LoginInterface(Hospital hospital) {
        this.hospital = hospital;
        this.controller = new LoginController(hospital);
        
        configurarJanela();
        criarComponentes();
        
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Hospital - Acesso ao Sistema");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void criarComponentes() {
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(41, 128, 185));
        JLabel lblLogin = new JLabel("LOGIN");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Arial", Font.BOLD, 22));
        painelTopo.add(lblLogin);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 10, 25));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

        painelCampos.add(new JLabel("E-mail:"));
        email = new JTextField();
        painelCampos.add(email);

        painelCampos.add(new JLabel("Senha:"));
        senha = new JPasswordField();
        
        senha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) autenticar();
            }
        });
        
        painelCampos.add(senha);

        JButton entrar = new JButton("Entrar no Sistema");
        entrar.setFont(new Font("Arial", Font.BOLD, 14));
        entrar.setBackground(new Color(41, 128, 185));
        entrar.setForeground(Color.WHITE);
        entrar.setFocusPainted(false);
        entrar.setPreferredSize(new Dimension(200, 40));
        entrar.addActionListener(e -> autenticar());

        JPanel painelBotao = new JPanel(new FlowLayout());
        painelBotao.add(entrar);

        add(painelCampos, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);
    }

    private void autenticar() {
        try {
            Usuario u = controller.autenticar(
                email.getText(),
                new String(senha.getPassword())
            );

            dispose();

            // ROTEAMENTO CORRIGIDO
            if (u instanceof Paciente) {
                new PacienteInterface((Paciente) u, this.hospital).setVisible(true);
            } 
            else if (u instanceof Medico) {
                
                new MedicoInterface(this.hospital, (Medico) u).setVisible(true);
            } 
            else if (u instanceof Recepcionista) {
                new SecretariaInterface((Recepcionista) u, this.hospital).setVisible(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Acesso Negado: " + e.getMessage(), 
                "Erro de Login", 
                JOptionPane.ERROR_MESSAGE);
            
            senha.setText("");
            senha.requestFocus();
        }
    }
}