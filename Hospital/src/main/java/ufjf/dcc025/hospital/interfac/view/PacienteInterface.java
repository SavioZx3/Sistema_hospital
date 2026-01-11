package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class PacienteInterface extends JFrame {

    private Paciente paciente;

    public PacienteInterface(Usuario u) {
        this.paciente = (Paciente) u;

        setTitle("Área do Paciente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton consultas = new JButton("Minhas Consultas");
        JButton receitas = new JButton("Minhas Receitas");
        JButton atestados = new JButton("Meus Atestados");
        JButton historico = new JButton("Histórico Médico");

        consultas.addActionListener(e ->
            paciente.getHistorico().getConsultas()
        );

        historico.addActionListener(e ->
            paciente.getHistorico().getExames()
        );

        JPanel p = new JPanel();
        p.add(consultas);
        p.add(receitas);
        p.add(atestados);
        p.add(historico);

        add(p);
        setVisible(true);
    }
}