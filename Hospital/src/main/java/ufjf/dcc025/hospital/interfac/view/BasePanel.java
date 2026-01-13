package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {

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
