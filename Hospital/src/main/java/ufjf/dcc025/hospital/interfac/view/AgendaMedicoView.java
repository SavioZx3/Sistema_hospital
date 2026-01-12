package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.consulta.StatusConsulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class AgendaMedicoView extends JFrame {
    private Medico medico;
    private Hospital hospital;
    private DefaultTableModel model;

    public AgendaMedicoView(Medico medico, Hospital hospital) {
        this.medico = medico;
        this.hospital = hospital;
        
        setTitle("Agenda - Dr. " + medico.getNome());
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Apenas fecha esta janela

        model = new DefaultTableModel(new String[]{"Hora", "Paciente", "Status"}, 0);
        JTable tabela = new JTable(model);
        
        JButton btnAtender = new JButton("Iniciar Atendimento");
        btnAtender.addActionListener(e -> {
            int fila = tabela.getSelectedRow();
            if (fila != -1) {
                // Pega a consulta da lista do médico baseada na linha selecionada
                Consulta c = medico.getAgenda().getConsultas().get(fila);
                if (c.getStatus() == StatusConsulta.AGENDADA || c.getStatus() == StatusConsulta.EM_ANDAMENTO) {
                    new AtendimentoMedicoView(this, medico, c).setVisible(true);
                    hospital.atualizarConsultasHospital(medico);
                    atualizar();
                } else {
                    JOptionPane.showMessageDialog(this, "Esta consulta está " + c.getStatus());
                }
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(btnAtender, BorderLayout.SOUTH);
        
        atualizar();
        setVisible(true); // ESSENCIAL: Faz a janela aparecer!
    }

    private void atualizar() {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        
        // Agora mostra TODAS as consultas para o médico saber o que tem livre também
        for (Consulta c : medico.getAgenda().getConsultas()) {
            String nomePaciente = (c.getPaciente() != null) ? c.getPaciente().getNome() : "--- LIVRE ---";
            model.addRow(new Object[]{
                c.getDataHoraInicio().format(dtf), 
                nomePaciente, 
                c.getStatus()
            });
        }
    }
}