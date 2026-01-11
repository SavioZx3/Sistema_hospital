package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;

public class EmitirAtestadoView extends JFrame {

    public EmitirAtestadoView(Medico medico) {

        Consulta consulta = medico.getAgenda1().getConsultas().get(0);

        String dias = JOptionPane.showInputDialog("Dias de afastamento:");

        try {
            consulta.emitirAtestado(Integer.parseInt(dias));
            JOptionPane.showMessageDialog(this,
                consulta.getAtestado().toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}