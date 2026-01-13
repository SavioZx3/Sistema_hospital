package ufjf.dcc025.hospital.interfac.view;

import ufjf.dcc025.hospital.Hospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseView extends JFrame {

    protected final Hospital hospital;

    public BaseView(Hospital hospital, String titulo) {
        this.hospital = hospital;

        setTitle(titulo);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        configurarLogoutPadrao();
    }

    private void configurarLogoutPadrao() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                executarLogout();
            }
        });
    }

    protected JPanel criarTopo(String tituloTela) {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(44, 62, 80));
        topo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitulo = new JLabel(tituloTela);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnLogout = new JButton("Sair / Logout");
        btnLogout.addActionListener(e -> executarLogout());

        topo.add(lblTitulo, BorderLayout.WEST);
        topo.add(btnLogout, BorderLayout.EAST);

        return topo;
    }

    protected void executarLogout() {
        int op = JOptionPane.showConfirmDialog(
                this,
                "Deseja encerrar a sessão?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            dispose();
            new LoginInterface(hospital);
        }
    }

    protected void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    protected void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }

    protected void mostrarInfo(String mensagem) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Informação",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
