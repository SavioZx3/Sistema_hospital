package ufjf.dcc025.hospital.interfac.view.medico;

/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/


import javax.swing.*;
import java.awt.*;

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class AtendimentoMedicoView extends JDialog {

    private final Medico medico;
    private final Consulta consulta;
    private final Runnable onFinalizar;
    private final Hospital hospital;

    private JTextField txtDiasAtestado;
    private JButton btnInternacao;

    public AtendimentoMedicoView(
            Window parent,
            Medico medico,
            Consulta consulta,
            Hospital hospital,
            Runnable onFinalizar
    ) {
        super(parent, "Prontuário: " + consulta.getPaciente().getNome(),
                ModalityType.APPLICATION_MODAL);

        this.medico = medico;
        this.consulta = consulta;
        this.hospital = hospital;
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

        container.add(new JLabel("Receita:"));
        JButton btnEmitirReceita = new JButton("EMITIR RECEITA");
        btnEmitirReceita.setFont(new Font("Arial", Font.BOLD, 13));
        btnEmitirReceita.setBackground(new Color(155, 89, 182));
        btnEmitirReceita.setForeground(Color.WHITE);
        btnEmitirReceita.addActionListener(e -> emitirReceita());
        container.add(btnEmitirReceita);


        txtDiasAtestado = criarCampo(container, "Dias de Atestado:", "0");
        container.add(new JLabel("Atestado:"));
        JButton btnEmitirAtestado = new JButton("EMITIR ATESTADO");
        btnEmitirAtestado.setFont(new Font("Arial", Font.BOLD, 13));
        btnEmitirAtestado.setBackground(new Color(241, 196, 15));
        btnEmitirAtestado.setForeground(Color.BLACK);
        btnEmitirAtestado.addActionListener(e -> emitirAtestado());
        container.add(btnEmitirAtestado);


        container.add(new JLabel("Exames:"));
        JButton btnEmitirExame = new JButton("EMITIR EXAME");
        btnEmitirExame.setFont(new Font("Arial", Font.BOLD, 13));
        btnEmitirExame.setBackground(new Color(52, 152, 219));
        btnEmitirExame.setForeground(Color.WHITE);
        btnEmitirExame.addActionListener(e -> emitirExame());
        container.add(btnEmitirExame);


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

    private JTextField criarCampo(JPanel p, String rotulo, String valor) {
        p.add(new JLabel(rotulo));
        JTextField campo = new JTextField(valor);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        p.add(campo);
        p.add(Box.createVerticalStrut(10));
        return campo;
    }

    private void emitirReceita() {
        try {
            consulta.emitirReceita();
            JOptionPane.showMessageDialog(
                    this,
                    "Receita emitida com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void emitirAtestado() {
    try {
        int dias = Integer.parseInt(txtDiasAtestado.getText());

        consulta.emitirAtestado(dias);

        JOptionPane.showMessageDialog(
                this,
                "Atestado emitido por " + dias + " dias.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}



    private void emitirExame() {
        try {
            consulta.emitirExame();
            JOptionPane.showMessageDialog(
                    this,
                    "Exame emitido com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
        boolean internado
                = consulta.getPaciente().getStatusInternacao() == StatusInternacao.INTERNADO;

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
            consulta.marcarConcluida();
            consulta.getPaciente().getHistorico().adicionaConsulta(consulta);

            if (onFinalizar != null) {
                onFinalizar.run();
            }

            JOptionPane.showMessageDialog(this, "Atendimento finalizado e salvo com sucesso!");
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dias de atestado inválido. Use apenas números.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + e.getMessage());
        }
    }
}

