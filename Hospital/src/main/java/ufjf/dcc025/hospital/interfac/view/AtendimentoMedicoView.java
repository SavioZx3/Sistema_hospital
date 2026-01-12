package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class AtendimentoMedicoView extends JDialog {

    private Medico medico;
    private Consulta consulta;
    private JTextField txtDiasAtestado, txtRemedio, txtDose;
    private boolean finalizado = false;

    public AtendimentoMedicoView(Frame parent, Medico medico, Consulta consulta) {
        super(parent, "Atendimento: " + consulta.getPaciente().getNome(), true);
        this.medico = medico;
        this.consulta = consulta;

        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Tenta iniciar a consulta no modelo de dados ao abrir a tela
        try {
            consulta.marcarInicioConsulta();
        } catch (IllegalStateException e) {
            // Se já estiver em andamento, apenas ignora
        }

        criarFormulario();
    }

    private void criarFormulario() {
        JPanel painel = new JPanel(new GridLayout(6, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Medicamento:"));
        txtRemedio = new JTextField();
        painel.add(txtRemedio);

        painel.add(new JLabel("Dosagem/Instruções:"));
        txtDose = new JTextField();
        painel.add(txtDose);

        painel.add(new JLabel("Dias de Atestado (0 se não houver):"));
        txtDiasAtestado = new JTextField("0");
        painel.add(txtDiasAtestado);

        JButton btnFinalizar = new JButton("Finalizar Atendimento e Emitir Documentos");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnFinalizar.setBackground(new Color(34, 139, 34));
        btnFinalizar.setForeground(Color.WHITE);

        btnFinalizar.addActionListener(e -> concluir());

        add(painel, BorderLayout.CENTER);
        add(btnFinalizar, BorderLayout.SOUTH);
    }

    private void concluir() {
        try {
            // Validação simples
            if (txtRemedio.getText().trim().isEmpty() || txtDose.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o medicamento e a dose.");
                return;
            }

            int dias = Integer.parseInt(txtDiasAtestado.getText().trim());
            String remedio = txtRemedio.getText().trim();
            String dose = txtDose.getText().trim();

            // Executa a lógica de negócio do Médico
            // Isso internamente chama emitirAtestado, emitirReceita e marcarConcluida
            medico.realizarAtendimento(consulta, dias, remedio, dose, LocalDate.now().plusMonths(1));

            JOptionPane.showMessageDialog(this, "Atendimento finalizado e documentos gerados!");
            finalizado = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dias de atestado deve ser um número inteiro.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar atendimento: " + ex.getMessage());
        }
    }
    
    public boolean isFinalizado() { return finalizado; }
}
