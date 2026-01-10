package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica da área da recepcionista.
 */
public class SecretariaInterface extends JFrame {

    public SecretariaInterface() {
        setTitle("Sistema Hospitalar - Área da Recepcionista");
        setSize(520, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarCabecalho();
        criarMenu();
        criarRodape();

        setVisible(true);
    }

    private void criarCabecalho() {
        JLabel titulo = new JLabel("Área da Recepcionista", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);
    }

    private void criarMenu() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 12, 12));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton btnCadastrarPaciente = new JButton("👤 Cadastrar Paciente");
        JButton btnAgendarConsulta = new JButton("📅 Agendar Consulta");
        JButton btnVisualizarConsultas = new JButton("📋 Visualizar Consultas");
        JButton btnGerenciarMedicos = new JButton("🩺 Gerenciar Médicos");

        btnCadastrarPaciente.addActionListener(e ->
                mostrarMensagem("Cadastro de paciente.")
        );

        btnAgendarConsulta.addActionListener(e ->
                mostrarMensagem("Agendamento de consulta.")
        );

        btnVisualizarConsultas.addActionListener(e ->
                mostrarMensagem("Lista de consultas agendadas.")
        );

        btnGerenciarMedicos.addActionListener(e ->
                mostrarMensagem("Gerenciamento de médicos.")
        );

        painel.add(btnCadastrarPaciente);
        painel.add(btnAgendarConsulta);
        painel.add(btnVisualizarConsultas);
        painel.add(btnGerenciarMedicos);

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
                mensagem + "\n(Funcionalidade em desenvolvimento",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}