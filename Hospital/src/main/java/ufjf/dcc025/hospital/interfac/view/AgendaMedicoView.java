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
    private final Medico medico;
    private final Hospital hospital;
    private final DefaultTableModel model;
    private final JTable tabela;

    public AgendaMedicoView(Medico medico, Hospital hospital) {
        this.medico = medico;
        this.hospital = hospital;
        
        setTitle("Agenda - Dr. " + medico.getNome());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"ID", "Hora", "Paciente", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JButton btnAtender = new JButton(" INICIAR ATENDIMENTO SELECIONADO");
        btnAtender.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAtender.setBackground(new Color(41, 128, 185));
        btnAtender.setForeground(Color.WHITE);

        btnAtender.addActionListener(e -> {
            int fila = tabela.getSelectedRow();
            if (fila != -1) {
                Consulta c = medico.getAgenda().getConsultas().get(fila);
                
                if (c.getStatus() == StatusConsulta.AGENDADA || c.getStatus() == StatusConsulta.EM_ANDAMENTO) {
                    AtendimentoMedicoView atendimento = new AtendimentoMedicoView(this, medico, c);
                    atendimento.setVisible(true);
                    
                    if (atendimento.isFinalizado()) {
                        atualizar();
                    }
                } else if (c.getStatus() == StatusConsulta.REALIZADA) {
                    JOptionPane.showMessageDialog(this, "Este atendimento já foi concluído.");
                } else {
                    JOptionPane.showMessageDialog(this, "Não há paciente agendado para este horário.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha na tabela.");
            }
        });

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(btnAtender, BorderLayout.SOUTH);
        
        atualizar();
        setVisible(true);
    }

    private void atualizar() {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        
        int id = 0;
        for (Consulta c : medico.getAgenda().getConsultas()) {
            String nomePac = (c.getPaciente() != null) ? c.getPaciente().getNome() : "---";
            model.addRow(new Object[]{
                id++,
                c.getDataHoraInicio().format(dtf), 
                nomePac, 
                c.getStatus()
            });
        }
    }
}