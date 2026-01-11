package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class MedicoInterface extends JFrame {

    private Medico medico;

    public MedicoInterface(Usuario usuario) {
        this.medico = (Medico) usuario;

        setTitle("Área do Médico");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton consultas = new JButton("Ver Consultas");
        JButton pacientes = new JButton("Ver Pacientes");
        JButton receita = new JButton("Emitir Receita");
        JButton atestado = new JButton("Emitir Atestado");

        consultas.addActionListener(e ->
            new ConsultasMedicoView(medico)
        );

        pacientes.addActionListener(e ->
            medico.getAgenda1().getConsultas()
        );

        receita.addActionListener(e ->
            new EmitirReceitaView(medico)
        );

        atestado.addActionListener(e ->
            new EmitirAtestadoView(medico)
        );

        painel.add(consultas);
        painel.add(pacientes);
        painel.add(receita);
        painel.add(atestado);

        add(painel);
        setVisible(true);
    }
}