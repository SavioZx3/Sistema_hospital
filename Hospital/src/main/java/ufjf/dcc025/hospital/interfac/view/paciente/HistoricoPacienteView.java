package ufjf.dcc025.hospital.interfac.view.paciente;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class HistoricoPacienteView extends JDialog {

    public HistoricoPacienteView(Window parent, Paciente paciente) {
        super(parent, "Histórico Clínico: " + paciente.getNome(), ModalityType.APPLICATION_MODAL);

        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JTextArea txtHistorico = new JTextArea();
        txtHistorico.setEditable(false);
        txtHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtHistorico.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtHistorico.setLineWrap(true);       // Ativa quebra de linha
        txtHistorico.setWrapStyleWord(true);  // Garante que a quebra não corte palavras ao meio

        txtHistorico.setText(paciente.getHistorico().toString());

        add(new JLabel("  Prontuário Completo do Paciente", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(txtHistorico), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);
    }
}
