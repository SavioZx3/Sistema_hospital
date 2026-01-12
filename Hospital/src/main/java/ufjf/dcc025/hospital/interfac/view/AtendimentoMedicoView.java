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
        super(parent, "Prontuário: " + consulta.getPaciente().getNome(), ModalityType.APPLICATION_MODAL);
        this.medico = medico;
        this.consulta = consulta;

        // Tenta iniciar a consulta automaticamente ao abrir a tela
        try {
            if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO) {
                consulta.marcarInicioConsulta(); 
            }
        } catch (Exception e) {
            // Se falhar (ex: horário expirado), mostramos o erro mas permitimos visualizar
            System.err.println("Erro ao iniciar atendimento: " + e.getMessage());
        }

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        criarFormulario();
    }

    private void criarFormulario() {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelCorpo = new JPanel();
        painelCorpo.setLayout(new BoxLayout(painelCorpo, BoxLayout.Y_AXIS));

        painelCorpo.add(new JLabel("💊 Medicamento:"));
        txtRemedio = new JTextField();
        txtRemedio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtRemedio);
        
        painelCorpo.add(Box.createVerticalStrut(10));
        painelCorpo.add(new JLabel("📝 Dosagem:"));
        txtDose = new JTextField();
        txtDose.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtDose);

        painelCorpo.add(Box.createVerticalStrut(10));
        painelCorpo.add(new JLabel("📅 Dias de Atestado (use 0 para nenhum):"));
        txtDiasAtestado = new JTextField("0");
        txtDiasAtestado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painelCorpo.add(txtDiasAtestado);

        painelCorpo.add(Box.createVerticalStrut(20));
        btnInternacao = new JButton();
        atualizarTextoInternacao();
        btnInternacao.addActionListener(e -> alternarInternacao());
        painelCorpo.add(btnInternacao);

        container.add(painelCorpo, BorderLayout.CENTER);

        JButton btnFinalizar = new JButton("FINALIZAR E SALVAR NO HISTÓRICO");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 13));
        btnFinalizar.setBackground(new Color(34, 139, 34));
        btnFinalizar.setForeground(Color.WHITE);
        btnFimDesign(btnFinalizar);
        btnFinalizar.addActionListener(e -> concluir());

        add(container, BorderLayout.CENTER);
        add(btnFinalizar, BorderLayout.SOUTH);
    }

    private void btnFimDesign(JButton btn) {
        btn.setPreferredSize(new Dimension(0, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            String remedio = txtRemedio.getText().trim();
            String dose = txtDose.getText().trim();
            int dias = Integer.parseInt(txtDiasAtestado.getText().trim());

            // 1. Garantir que a consulta está em andamento para poder emitir documentos
            if (consulta.getStatus() != StatusConsulta.EM_ANDAMENTO && consulta.getStatus() != StatusConsulta.REALIZADA) {
                consulta.marcarInicioConsulta();
            }

            // 2. Emissão condicional baseada na lógica da classe Consulta
            // Note: Use os nomes que você definiu na classe Consulta (ex: emitirAtestado)
            if (dias > 0) {
                consulta.getAtestado(dias); 
            }

            if (!remedio.isEmpty()) {
                consulta.getReceita(remedio, dose, LocalDate.now().plusMonths(1));
            }

            // 3. Mudar status para REALIZADA
            consulta.marcarConcluida();

            // 4. Persistir no Histórico do Paciente
            if (consulta.getPaciente() != null) {
                consulta.getPaciente().getHistorico().adicionaConsulta(consulta);
            }

            JOptionPane.showMessageDialog(this, "Atendimento Finalizado com Sucesso!");
            finalizado = true;
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O campo Dias de Atestado deve conter apenas números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + ex.getMessage());
        }
    }

    public boolean isFinalizado() { return finalizado; }
}