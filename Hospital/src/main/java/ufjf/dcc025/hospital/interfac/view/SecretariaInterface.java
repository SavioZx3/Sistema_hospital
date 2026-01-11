package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import ufjf.dcc025.hospital.usuarios.Usuario;

public class SecretariaInterface extends JFrame {

    public SecretariaInterface(Usuario u) {

        setTitle("Área da Recepção");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton cadastrar = new JButton("Cadastrar Paciente");
        JButton agendar = new JButton("Agendar Consulta");
        JButton listar = new JButton("Listar Consultas");
        JButton medicos = new JButton("Gerenciar Médicos");

        cadastrar.addActionListener(e -> {});
        agendar.addActionListener(e -> {});
        listar.addActionListener(e -> {});
        medicos.addActionListener(e -> {});

        JPanel p = new JPanel();
        p.add(cadastrar);
        p.add(agendar);
        p.add(listar);
        p.add(medicos);

        add(p);
        setVisible(true);
    }
}
