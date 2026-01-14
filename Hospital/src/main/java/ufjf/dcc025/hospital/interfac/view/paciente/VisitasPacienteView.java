/*
    Mateus Gomes da Silva Matrícula: 202465565C 
    Marcos José De Oliveira Júnior Matrícula: 202135011 
    Sávio Machado Lima Matrícula: 202435014
*/
package ufjf.dcc025.hospital.interfac.view.paciente;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusVisita;

/**
 * Interface para consulta de permissão de visita (pelo CPF do paciente).
 */
public class VisitasPacienteView extends JPanel {

    private Hospital hospital;
    private JTextField campoBusca;
    private JLabel lblStatusResultado;
    private JLabel lblNomePaciente;

    public VisitasPacienteView(Hospital hospital) {
        this.hospital = hospital;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO ---
        JLabel titulo = new JLabel("Consulta de Visitas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        // --- CAMPO DE BUSCA ---
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Digite o CPF do Paciente:"), gbc);

        campoBusca = new JTextField(15);
        gbc.gridx = 1;
        add(campoBusca, gbc);

        // --- BOTÃO ---
        JButton btnBuscar = new JButton("Verificar Status");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(btnBuscar, gbc);

        // --- RESULTADOS ---
        lblNomePaciente = new JLabel(" ", SwingConstants.CENTER);
        lblNomePaciente.setFont(new Font("Arial", Font.ITALIC, 14));
        gbc.gridy = 3;
        add(lblNomePaciente, gbc);

        lblStatusResultado = new JLabel("Insira o CPF para consultar", SwingConstants.CENTER);
        lblStatusResultado.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 4;
        add(lblStatusResultado, gbc);

        // --- AÇÃO ---
        btnBuscar.addActionListener(e -> realizarBusca());
    }

    private void realizarBusca() {
        String cpf = campoBusca.getText().trim();

        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um CPF.");
            return;
        }

        try {
            // Busca o paciente no hospital
            Paciente p = hospital.buscarPacientePorCpf(cpf);

            if (p != null) {

                lblNomePaciente.setText("Paciente: " + p.getNome());

                // Verifica o Enum StatusVisita que o médico gerencia
                if (p.getStatusVisita() == StatusVisita.APTO) {
                    lblStatusResultado.setText("VISITA AUTORIZADA");
                    lblStatusResultado.setForeground(new Color(0, 128, 0)); // Verde
                } else {
                    lblStatusResultado.setText("VISITA NÃO AUTORIZADA");
                    lblStatusResultado.setForeground(Color.RED);
                }
            }

        } catch (Exception ex) {
            lblNomePaciente.setText(" ");
            lblStatusResultado.setText(" Paciente não encontrado.");
            lblStatusResultado.setForeground(Color.GRAY);
        }
    }
}
