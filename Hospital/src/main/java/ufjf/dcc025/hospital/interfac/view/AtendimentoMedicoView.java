package ufjf.dcc025.hospital.interfac.view;

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
    private JTextField txtDiasAtestado, txtRemedio, txtDose;
    private JButton btnInternacao;
    private boolean finalizado = false; // VARIAVEL DE CONTROLE

    public AtendimentoMedicoView(Window parent, Medico medico, Consulta consulta) {
        super(parent, "Prontuário: " + consulta.getPaciente().getNome(), ModalityType.APPLICATION_MODAL);
        this.medico = medico;
        this.consulta = consulta;

        try {
            if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO) {
                consulta.marcarInicioConsulta(); 
            }
        } catch (Exception e) {
            System.err.println("Erro ao iniciar: " + e.getMessage());
        }

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        criarFormulario();
    }

    public boolean isFinalizado() {
        return this.finalizado;
    }

    private void criarFormulario() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        container.add(new JLabel(" Medicamento:"));
        txtRemedio = new JTextField();
        txtRemedio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        container.add(txtRemedio);
        
        container.add(Box.createVerticalStrut(10));
        container.add(new JLabel(" Dosagem:"));
        txtDose = new JTextField();
        txtDose.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        container.add(txtDose);

        container.add(Box.createVerticalStrut(10));
        container.add(new JLabel(" Dias de Atestado:"));
        txtDiasAtestado = new JTextField("0");
        txtDiasAtestado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        container.add(txtDiasAtestado);

        container.add(Box.createVerticalStrut(20));
        btnInternacao = new JButton();
        atualizarTextoInternacao();
        btnInternacao.addActionListener(e -> alternarInternacao());
        btnInternacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btnInternacao);

        JButton btnFinalizar = new JButton("FINALIZAR E SALVAR ATENDIMENTO");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 13));
        btnFinalizar.setBackground(new Color(34, 139, 34));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setPreferredSize(new Dimension(0, 60));
        btnFinalizar.addActionListener(e -> concluir());

        add(container, BorderLayout.CENTER);
        add(btnFinalizar, BorderLayout.SOUTH);
    }

    private void alternarInternacao() {
        if (consulta.getPaciente().getStatusInternacao() == StatusInternacao.INTERNADO) {
            consulta.getPaciente().setStatusInternacao(StatusInternacao.ALTA);
        } else {
            consulta.getPaciente().setStatusInternacao(StatusInternacao.INTERNADO);
        }
        atualizarTextoInternacao();
    }

    private void atualizarTextoInternacao() {
        if (consulta.getPaciente().getStatusInternacao() == StatusInternacao.INTERNADO) {
            btnInternacao.setText("PACIENTE INTERNADO (Dar Alta)");
            btnInternacao.setBackground(new Color(255, 107, 107));
            btnInternacao.setForeground(Color.WHITE);
        } else {
            btnInternacao.setText("PACIENTE EM ALTA (Internar)");
            btnInternacao.setBackground(new Color(200, 200, 200));
            btnInternacao.setForeground(Color.BLACK);
        }
    }

    private void concluir() {
        try {
            int dias = Integer.parseInt(txtDiasAtestado.getText().trim());
            String remedio = txtRemedio.getText().trim();
            String dose = txtDose.getText().trim();

            if (dias > 0) consulta.getAtestado(dias);
            if (!remedio.isEmpty()) consulta.getReceita(remedio, dose, LocalDate.now().plusMonths(1));

            consulta.marcarConcluida();
            if (consulta.getPaciente() != null) {
                consulta.getPaciente().getHistorico().adicionaConsulta(consulta);
            }

            this.finalizado = true; 
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}