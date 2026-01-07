package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

/**
 * Interface da recepcionista.
 * Responsável por agendamento e gerenciamento de consultas.
 */
public class SecretariaInterface extends JFrame{

    private CardLayout cardLayout;
    private JPanel painelConteudo;

    public SecretariaInterface(){
        setTitle("Sistema Hospitalar - Secretaria");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarMenuLateral();
        criarPainelConteudo();

        setVisible(true);
    }

    private void criarMenuLateral(){
        JPanel menu = new JPanel(new GridLayout(5, 1, 10, 10));
        menu.setPreferredSize(new Dimension(200, 0));

        JButton btnGerenciar = new JButton("Gerenciamento de Dados");
        JButton btnVisitas = new JButton("Controle de visitas");
        JButton btnAgenda = new JButton("Agenda de consultas");
        JButton btnSair = new JButton("Sair");

        btnGerenciar.addActionListener(e -> cardLayout.show(painelConteudo, "GERENCIAR"));
        btnVisitas.addActionListener(e -> cardLayout.show(painelConteudo, "VISITAS"));
        btnAgenda.addActionListener(e -> cardLayout.show(painelConteudo, "AGENDA"));
        btnSair.addActionListener(e -> sair());

        menu.add(btnGerenciar);
        menu.add(btnVisitas);
        menu.add(btnAgenda);
        menu.add(new JLabel());
        menu.add(btnSair);

        add(menu, BorderLayout.WEST);
    }

    private void criarPainelConteudo(){
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        painelConteudo.add(new GerenciamentoDadosView(), "GERENCIAR");
        painelConteudo.add(new ControleVisitasView(), "VISITAS");
        painelConteudo.add(new AgendaConsultasView(), "AGENDA");

        add(painelConteudo, BorderLayout.CENTER);
    }

    private void sair(){
        dispose();
        new LoginInterface();
    }
}