package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;

public abstract class BasePanel extends JPanel {

    // ================= MÉTODOS PRINCIPAIS =================

    protected void mostrarErro(String mensagem) {
        mostrarMensagem(mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    protected void mostrarAviso(String mensagem) {
        mostrarMensagem(mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    protected void mostrarInfo(String mensagem) {
        mostrarMensagem(mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= SOBRECARGA PARA EXCEÇÕES =================

    protected void mostrarErro(Exception e) {
        mostrarErro(e.getMessage());
    }

    protected void mostrarAviso(Exception e) {
        mostrarAviso(e.getMessage());
    }

    // ================= MÉTODO CENTRAL =================

    private void mostrarMensagem(String mensagem, String titulo, int tipo) {
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                titulo,
                tipo
        );
    }
}
