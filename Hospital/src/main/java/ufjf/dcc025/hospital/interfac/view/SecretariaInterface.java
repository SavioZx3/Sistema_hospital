package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista; // Certifique-se de importar
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;

public class SecretariaInterface extends JFrame {
    private final Hospital hospital;
    private final Recepcionista recepcionistaLogada; // Campo adicionado

    // Construtor corrigido para aceitar a Recepcionista e o Hospital
    public SecretariaInterface(Recepcionista recepcionista, Hospital hospital) {
        this.recepcionistaLogada = recepcionista;
        this.hospital = hospital;
        
        setTitle("Portal da Secretaria - Operador: " + recepcionistaLogada.getNome());
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barra de Menu para Logout
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSessao = new JMenu("Sessão");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            dispose();
            new LoginInterface(hospital).setVisible(true);
        });
        menuSessao.add(itemSair);
        menuBar.add(menuSessao);
        setJMenuBar(menuBar);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Agendamentos", criarPainelAgendamento());
        abas.addTab("Internação", criarPainelInternacao());

        add(abas);
        setVisible(true);
    }

    private JPanel criarPainelAgendamento() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        // Sua lógica de agendamento aqui...
        return painel;
    }

    private JPanel criarPainelInternacao() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        // Sua lógica de internação aqui...
        return painel;
    }
}