package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica da área do paciente.
 */
public class PacienteInterface extends JFrame {

    public PacienteInterface() {
        setTitle("Sistema Hospitalar - Área do Paciente");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarCabecalho();
        criarMenu();
        criarRodape();

        setVisible(true);
    }

    private void criarCabecalho() {
        JLabel titulo = new JLabel("Área do Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void criarMenu() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnConsultas = new JButton("📅 Minhas Consultas");
        JButton btnReceitas = new JButton("💊 Minhas Receitas");
        JButton btnAtestados = new JButton("🧾 Meus Atestados");
        JButton btnHistorico = new JButton("📖 Meu Histórico Médico");

        btnConsultas.addActionListener(e ->
                mostrarMensagem("Exibição das consultas do paciente.")
        );

        btnReceitas.addActionListener(e ->
                mostrarMensagem("Exibição das receitas médicas.")
        );

        btnAtestados.addActionListener(e ->
                mostrarMensagem("Exibição dos atestados médicos.")
        );

        btnHistorico.addActionListener(e ->
                mostrarMensagem("Histórico médico do paciente.")
        );

        painel.add(btnConsultas);
        painel.add(btnReceitas);
        painel.add(btnAtestados);
        painel.add(btnHistorico);

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