package ufjf.dcc025.hospital.interfac.view.secretaria;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class MarcacaoConsultaView extends JDialog {

    private final Hospital hospital;

    private JComboBox<Paciente> cbPaciente;
    private JComboBox<Consulta> cbConsulta;

    public MarcacaoConsultaView(Window owner, Hospital hospital, Runnable onClose) {
        super(owner, "Marcar Consulta", ModalityType.APPLICATION_MODAL);
        this.hospital = hospital;

        setSize(450, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        criarFormulario(onClose);
        setVisible(true);
    }

    private void criarFormulario(Runnable onClose) {
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbPaciente = new JComboBox<>();
        cbConsulta = new JComboBox<>();

        for (Paciente p : hospital.listarPacientes()) {
            cbPaciente.addItem(p);
        }

        for (Medico m : hospital.listarMedicos()) {
            for (Consulta c : m.getAgenda().getConsultas()) {
                if (c.getPaciente() == null) {
                    cbConsulta.addItem(c);
                }
            }
        }

        form.add(new JLabel("Paciente:"));
        form.add(cbPaciente);
        form.add(new JLabel("Consulta disponível:"));
        form.add(cbConsulta);

        JButton btnConfirmar = new JButton("Confirmar Agendamento");
        btnConfirmar.addActionListener(e -> {
            try {
                Paciente p = (Paciente) cbPaciente.getSelectedItem();
                Consulta c = (Consulta) cbConsulta.getSelectedItem();

                hospital.agendarConsultaNaAgenda(p, c.getMedico(), c);
                JOptionPane.showMessageDialog(this, "Consulta marcada com sucesso!");
                dispose();
                onClose.run();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(form, BorderLayout.CENTER);
        add(btnConfirmar, BorderLayout.SOUTH);
    }
}
