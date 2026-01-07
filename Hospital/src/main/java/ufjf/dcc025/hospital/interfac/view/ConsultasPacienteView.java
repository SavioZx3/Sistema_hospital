package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class ConsultasPacienteView extends JPanel {

    public ConsultasPacienteView() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Consultas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(2, 1, 20, 20));

        centro.add(criarConsultasAgendadas());
        centro.add(criarNovaConsulta());

        add(centro, BorderLayout.CENTER);
    }

    private JPanel criarConsultasAgendadas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Consultas agendadas"));

        String[] colunas = {"Médico", "Especialidade", "Data", "Cancelar"};
        Object[][] dados = {
                {"Dr. Carlos", "Clínico", "10/01", "X"},
                {"Dra. Ana", "Pediatria", "15/01", "X"}
        };

        JTable tabela = new JTable(dados, colunas);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarNovaConsulta() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nova consulta"));

        String[] colunas = {"Médico", "Especialidade", "Horário", "Agendar"};
        Object[][] dados = {
                {"Dr. Carlos", "Clínico", "09:00", "✔"},
                {"Dra. Ana", "Pediatria", "14:30", "✔"}
        };

        JTable tabela = new JTable(dados, colunas);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        return panel;
    }
}