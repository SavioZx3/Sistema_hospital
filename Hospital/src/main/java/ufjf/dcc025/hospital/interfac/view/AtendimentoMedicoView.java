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
    private boolean finalizado = false;

    public AtendimentoMedicoView(Window parent, Medico medico, Consulta consulta) {
        super(parent, "Prontuário Eletrônico: " + consulta.getPaciente().getNome(), ModalityType.APPLICATION_MODAL);
        this.medico = medico;
        this.consulta = consulta;

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        try {
            if (!consulta.getStatus().equals(StatusConsulta.EM_ANDAMENTO)) {
                consulta.marcarInicioConsulta();
            }
        } catch (Exception e) {
            // Se já estiver em andamento, apenas ignora
        }

        criarFormulario();
    }

    private void criarFormulario() {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Atendimento: " + consulta.getPaciente().getNome(), JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        container.add(titulo, BorderLayout.NORTH);

        JPanel painelCorpo = new JPanel();
        painelCorpo.setLayout(new BoxLayout(painelCorpo, BoxLayout.Y_AXIS));

        // Campos de Receita
        painelCorpo.add(new JLabel(" Medicamento:"));
        txtRemedio = new JTextField();
        txtRemedio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtRemedio);
        painelCorpo.add(Box.createVerticalStrut(10));

        painelCorpo.add(new JLabel(" Dosagem e Instruções:"));
        txtDose = new JTextField();
        txtDose.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtDose);
        painelCorpo.add(Box.createVerticalStrut(10));

        // Campo de Atestado
        painelCorpo.add(new JLabel(" Dias de Atestado (0 para nenhum):"));
        txtDiasAtestado = new JTextField("0");
        txtDiasAtestado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtDiasAtestado);
        painelCorpo.add(Box.createVerticalStrut(20));

        // Botão de Internação
        btnInternacao = new JButton();
        btnInternacao.setAlignmentX(Component.CENTER_ALIGNMENT);
        atualizarTextoInternacao();
        btnInternacao.addActionListener(e -> alternarInternacao());
        painelCorpo.add(btnInternacao);

        container.add(painelCorpo, BorderLayout.CENTER);

        JButton btnFinalizar = new JButton(" FINALIZAR E SALVAR NO HISTÓRICO");
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
        } else {
            btnInternacao.setText("CLIQUE PARA INTERNAR PACIENTE");
            btnInternacao.setBackground(new Color(200, 200, 200));
        }
    }

    private void concluir() {
        try {
            int dias = Integer.parseInt(txtDiasAtestado.getText().trim());
            String remedio = txtRemedio.getText().trim();
            String dose = txtDose.getText().trim();
         
            // 1. Emite Atestado (Apenas se > 0)
            if (dias > 0) {
                consulta.emitirAtestado(dias);
            }

            // 2. Emite Receita (Apenas se preenchido)
            if (!remedio.isEmpty()) {
                consulta.emitirReceita(remedio, dose, LocalDate.now().plusMonths(1));
            }

            // 3. Finaliza a consulta no modelo
            consulta.marcarConcluida();

            // 4. Salva no histórico do paciente
            consulta.getPaciente().getHistorico().adicionaConsulta(consulta);

            JOptionPane.showMessageDialog(this, "Atendimento finalizado com sucesso!");
            finalizado = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de dias inválido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + ex.getMessage());
        }
    }

    public boolean isFinalizado() { return finalizado; }
}