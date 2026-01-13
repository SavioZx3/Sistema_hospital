package ufjf.dcc025.hospital.interfac.view.medico;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;


public class AtendimentoMedicoView extends JDialog {

    private final Medico medico;
    private final Consulta consulta;
    private final Runnable onFinalizar;

    private JTextField txtDiasAtestado, txtRemedio, txtDose;
    private JTextArea txtExamesPedididos;
    private JButton btnInternacao;

    public AtendimentoMedicoView(
            Window parent,
            Medico medico,
            Consulta consulta,
            Runnable onFinalizar
    ) {
        super(parent, "Prontuário: " + consulta.getPaciente().getNome(),
                ModalityType.APPLICATION_MODAL);

        this.medico = medico;
        this.consulta = consulta;
        this.onFinalizar = onFinalizar;

        iniciarConsulta();

        setSize(520, 580);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        criarFormulario();
    }

    private void iniciarConsulta() {
        try {
            if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO) {
                consulta.marcarInicioConsulta();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao iniciar consulta: " + e.getMessage());
        }
    }

    // ======================= FORMULÁRIO =======================

    private void criarFormulario() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtRemedio = criarCampo(container, "Medicamento:");
        txtDose = criarCampo(container, "Dosagem:");
        txtDiasAtestado = criarCampo(container, "Dias de Atestado:", "0");

        container.add(new JLabel("Solicitar Exames:"));
        txtExamesPedididos = new JTextArea(3, 20);
        txtExamesPedididos.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        container.add(new JScrollPane(txtExamesPedididos));

        container.add(Box.createVerticalStrut(20));

        btnInternacao = new JButton();
        atualizarBotaoInternacao();
        btnInternacao.addActionListener(e -> alternarInternacao());
        container.add(btnInternacao);

        JButton btnFinalizar = new JButton("FINALIZAR ATENDIMENTO");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(39, 174, 96));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.addActionListener(e -> concluir());

        add(container, BorderLayout.CENTER);
        add(btnFinalizar, BorderLayout.SOUTH);
    }

    private JTextField criarCampo(JPanel p, String rotulo) {
        return criarCampo(p, rotulo, "");
    }

    private JTextField criarCampo(JPanel p, String rotulo, String valor) {
        p.add(new JLabel(rotulo));
        JTextField campo = new JTextField(valor);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        p.add(campo);
        p.add(Box.createVerticalStrut(10));
        return campo;
    }

    // ======================= INTERNAÇÃO =======================

    private void alternarInternacao() {
        if (consulta.getPaciente().getStatusInternacao() == StatusInternacao.INTERNADO) {
            consulta.getPaciente().setStatusInternacao(StatusInternacao.ALTA);
        } else {
            consulta.getPaciente().setStatusInternacao(StatusInternacao.INTERNADO);
        }
        atualizarBotaoInternacao();
    }

    private void atualizarBotaoInternacao() {
        boolean internado =
                consulta.getPaciente().getStatusInternacao() == StatusInternacao.INTERNADO;

        btnInternacao.setText(
                internado ? "PACIENTE INTERNADO (Dar Alta)"
                          : "PACIENTE EM ALTA (Internar)"
        );

        btnInternacao.setBackground(
                internado ? new Color(231, 76, 60)
                          : new Color(189, 195, 199)
        );
        btnInternacao.setForeground(Color.WHITE);
    }

    // ======================= FINALIZAÇÃO =======================

    private void concluir() {
        try {
            int dias = Integer.parseInt(txtDiasAtestado.getText().trim());

            if (dias > 0) consulta.getAtestado(dias);

            if (!txtRemedio.getText().isBlank()) {
                consulta.getReceita(
                        txtRemedio.getText(),
                        txtDose.getText(),
                        LocalDate.now().plusMonths(1)
                );
            }

            if (!txtExamesPedididos.getText().isBlank()) {
                consulta.setExame(txtExamesPedididos.getText());
            }

            consulta.marcarConcluida();
            consulta.getPaciente().getHistorico().adicionaConsulta(consulta);

            if (onFinalizar != null) {
                onFinalizar.run(); 
            }

            JOptionPane.showMessageDialog(this,
                    "Atendimento finalizado com sucesso!");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Dias de atestado inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao finalizar: " + e.getMessage());
        }
    }
}
