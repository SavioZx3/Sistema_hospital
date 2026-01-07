package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

/**
 * Interface gráfica do paciente.
 * Centraliza as telas:
 * - Minhas consultas
 * - Visitas
 * - Meus dados
 */
public class PacienteInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    public PacienteInterface() {
        setTitle("Sistema Hospitalar - Paciente");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarMenuLateral();
        criarPainelConteudo();

        setVisible(true);
    }

    private void criarMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(5, 1, 10, 10));
        menu.setPreferredSize(new Dimension(180, 0));

        JButton btnConsultas = new JButton("Minhas consultas");
        JButton btnVisitas = new JButton("Visitas");
        JButton btnDados = new JButton("Meus dados");
        JButton btnSair = new JButton("Sair");

        btnConsultas.addActionListener(e ->
                cardLayout.show(painelConteudo, "CONSULTAS"));
        btnVisitas.addActionListener(e ->
                cardLayout.show(painelConteudo, "VISITAS"));
        btnDados.addActionListener(e ->
                cardLayout.show(painelConteudo, "DADOS"));
        btnSair.addActionListener(e -> sair());

        menu.add(btnConsultas);
        menu.add(btnVisitas);
        menu.add(btnDados);
        menu.add(new JLabel());
        menu.add(btnSair);

        add(menu, BorderLayout.WEST);
    }

    private void criarPainelConteudo() {
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(new ConsultasPacienteView(), "CONSULTAS");
        painelConteudo.add(new VisitasPacienteView(), "VISITAS");
        painelConteudo.add(new DadosPacienteView(), "DADOS");

        add(painelConteudo, BorderLayout.CENTER);
    }

    private void sair() {
        dispose();              //fecha a jenela atual
        new LoginInterface();   //volta para tela de login
    }
}