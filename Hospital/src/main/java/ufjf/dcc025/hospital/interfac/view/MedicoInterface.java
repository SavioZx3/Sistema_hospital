package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica da área do médico.
 */
public class MedicoInterface extends JFrame {

    public MedicoInterface() {
        setTitle("Sistema Hospitalar - Área do Médico");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarCabecalho();
        criarMenu();
        criarRodape();

        setVisible(true);
    }

    private void criarCabecalho() {
        JLabel titulo = new JLabel("Área do Médico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void criarMenu() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnConsultas = new JButton("📋 Ver Consultas");
        JButton btnPacientes = new JButton("🧑‍🤝‍🧑 Ver Pacientes");
        JButton btnReceita = new JButton("📝 Emitir Receita");
        JButton btnAtestado = new JButton("🧾 Emitir Atestado");

        btnConsultas.addActionListener(e ->
                mostrarMensagem("Lista de consultas do médico.")
        );

        btnPacientes.addActionListener(e ->
                mostrarMensagem("Lista de pacientes atendidos.")
        );

        btnReceita.addActionListener(e ->
                mostrarMensagem("Emissão de receita médica.")
        );

        btnAtestado.addActionListener(e ->
                mostrarMensagem("Emissão de atestado médico.")
        );

        painel.add(btnConsultas);
        painel.add(btnPacientes);
        painel.add(btnReceita);
        painel.add(btnAtestado);

        add(painel, BorderLayout.CENTER);
    }

    private void criarRodape() {
        JPanel rodape = new JPanel();

        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginInterface();
        });

        rodape.add(btnLogout);
        add(rodape, BorderLayout.SOUTH);
    }

    private void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem + "\n(Funcionalidade em desenvolvimento)",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}