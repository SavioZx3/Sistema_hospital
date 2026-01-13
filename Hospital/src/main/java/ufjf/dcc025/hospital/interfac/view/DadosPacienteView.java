package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class DadosPacienteView extends JFrame {

    private Paciente paciente;
    private Hospital hospital;

    public DadosPacienteView(Paciente paciente, Hospital hospital) {
        this.paciente = paciente;
        this.hospital = hospital;

        setTitle("Prontuário e Dados - " + paciente.getNome());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Criar componentes
        add(criarCabecalho(), BorderLayout.NORTH);
        
        // Painel central com abas para organizar melhor
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Dados Pessoais", criarPainelDados());
        abas.addTab("Situação Hospitalar", criarPainelStatus());
        
        add(abas, BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new GridLayout(2, 1));
        JLabel titulo = new JLabel("Ficha do Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel subTitulo = new JLabel(paciente.getNome() + " | CPF: " + paciente.getCpf(), SwingConstants.CENTER);
        
        painel.add(titulo);
        painel.add(subTitulo);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return painel;
    }

    private JPanel criarPainelDados() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 15));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        painel.add(new JLabel("Telefone:"));
        painel.add(new JTextField(paciente.getTelefone()));

        painel.add(new JLabel("E-mail:"));
        painel.add(new JTextField(paciente.getEmail().toString()));

        painel.add(new JLabel("Endereço:"));
        JTextField txtEnd = new JTextField(paciente.getLogradouro());
        txtEnd.setEditable(false);
        painel.add(txtEnd);

        return painel;
    }

    private JPanel criarPainelStatus() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Mostra o Status de Internação atual
        JLabel lblInternacao = new JLabel(" Status Internação: " + paciente.getStatusInternacao().getDescricao());
        lblInternacao.setForeground(new Color(41, 128, 185));
        
        // Mostra o Status de Visita atual
        JLabel lblVisita = new JLabel(" Status Visita: " + paciente.getStatusVisita().getDescricao());
        
        // Botão para ver Histórico (Conectando com a classe Historico que configuramos)
        JButton btnVerHistorico = new JButton(" Ver Histórico Clínico (Receitas/Exames)");
        btnVerHistorico.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, paciente.getHistorico().toString(), 
                "Histórico de " + paciente.getNome(), JOptionPane.INFORMATION_MESSAGE);
        });

        painel.add(lblInternacao);
        painel.add(lblVisita);
        painel.add(new JSeparator());
        painel.add(btnVerHistorico);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel painel = new JPanel();
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painel.add(btnFechar);
        return painel;
    }
}