package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class MedicoInterface extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    public MedicoInterface() {
        setTitle("Sistema Hospitalar - Médico");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarMenuLateral();
        criarPainelConteudo();

        setVisible(true);
    }

    private void criarMenuLateral() {
        JPanel menu = new JPanel(new GridLayout(5, 1, 10, 10));
        menu.setPreferredSize(new Dimension(180, 0));

        JButton btnAgenda = new JButton("Minha agenda");
        JButton btnConsultas = new JButton("Minhas consultas");
        JButton btnVisitas = new JButton("Visitas");
        JButton btnSair = new JButton("Sair");

        btnAgenda.addActionListener(e -> cardLayout.show(painelConteudo, "AGENDA"));
        btnConsultas.addActionListener(e -> cardLayout.show(painelConteudo, "CONSULTAS"));
        btnVisitas.addActionListener(e -> cardLayout.show(painelConteudo, "VISITAS"));
        btnSair.addActionListener(e -> sair());

        menu.add(btnAgenda);
        menu.add(btnConsultas);
        menu.add(btnVisitas);
        menu.add(new JLabel());
        menu.add(btnSair);

        add(menu, BorderLayout.WEST);
    }

    private void criarPainelConteudo() {
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(new AgendaMedicoView(), "AGENDA");
        painelConteudo.add(new ConsultasMedicoView(), "CONSULTAS");
        painelConteudo.add(new VisitasMedicoView(), "VISITAS");

        add(painelConteudo, BorderLayout.CENTER);
    }

    private void sair() {
        dispose();              //fecha a jenela atual
        new LoginInterface();   //volta para tela de login
    }
}