package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.time.LocalDate;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;

public class EmitirReceitaView extends JFrame {

    public EmitirReceitaView(Medico medico) {

        Consulta consulta = medico.getAgenda1().getConsultas().get(0);

        String med = JOptionPane.showInputDialog("Medicamento:");
        String qtd = JOptionPane.showInputDialog("Quantidade:");

        try {
            consulta.emitirReceita(med, qtd, LocalDate.now().plusDays(30));
            JOptionPane.showMessageDialog(this,
                consulta.getReceita().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}