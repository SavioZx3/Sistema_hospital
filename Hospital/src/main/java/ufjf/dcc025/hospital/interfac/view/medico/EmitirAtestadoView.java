package ufjf.dcc025.hospital.interfac.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;

public class EmitirAtestadoView extends JFrame {
    // Recebe a CONSULTA como parametro 
    public EmitirAtestadoView(Consulta consulta) { 
        String dias = JOptionPane.showInputDialog("Dias de afastamento para " + consulta.getPaciente().getNome() + ":");
        if (dias != null) {
            try {
                int d = Integer.parseInt(dias);
                consulta.getAtestado(d);
                // IMPORTANTE: Adicionar ao histórico!
                consulta.getPaciente().adicionarAtestado(consulta.getAtestado());
                
                JOptionPane.showMessageDialog(this, "Atestado emitido com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }
    }
}