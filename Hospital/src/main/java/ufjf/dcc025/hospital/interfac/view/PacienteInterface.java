package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

/**
 * Interface gráfica da área do paciente - Versão Final Funcional.
 */
public class PacienteInterface extends JFrame {

    private Paciente pacienteLogado;
    private Hospital hospital;

    public PacienteInterface(Paciente paciente, Hospital hospital) {
        this.pacienteLogado = paciente;
        this.hospital = hospital;
        
        setTitle("Sistema Hospitalar - Área do Paciente: " + pacienteLogado.getNome());
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        criarCabecalho();
        criarMenu();
        criarRodape();

        setVisible(true);
    }

    private void criarCabecalho() {
        JPanel painelTitulo = new JPanel(new GridLayout(2, 1));
        JLabel titulo = new JLabel("Bem-vindo(a), " + pacienteLogado.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel subTitulo = new JLabel("CPF: " + pacienteLogado.getCpf(), SwingConstants.CENTER);
        subTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        painelTitulo.add(titulo);
        painelTitulo.add(subTitulo);
        
        add(painelTitulo, BorderLayout.NORTH);
    }

    private void criarMenu() {
        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnConsultas = new JButton("📅 Minhas Consultas");
        JButton btnReceitas = new JButton("💊 Minhas Receitas");
        JButton btnAtestados = new JButton("🧾 Meus Atestados");
        JButton btnExames = new JButton("🧪 Meus Exames");

        // --- AÇÃO CONSULTAS ---
        btnConsultas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre a view de consultas que já criamos
                new ConsultasPacienteView(pacienteLogado, hospital);
            }
        });

        // --- AÇÃO RECEITAS ---
        btnReceitas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirListaDados("Minhas Receitas", pacienteLogado.getReceitas().toArray());
            }
        });

        // --- AÇÃO ATESTADOS ---
        btnAtestados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirListaDados("Meus Atestados", pacienteLogado.getAtestados().toArray());
            }
        });

        // --- AÇÃO EXAMES ---
        btnExames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirListaDados("Meus Exames", pacienteLogado.getExames().toArray());
            }
        });

        painel.add(btnConsultas);
        painel.add(btnReceitas);
        painel.add(btnAtestados);
        painel.add(btnExames);

        add(painel, BorderLayout.CENTER);
    }

    /**
     * Método genérico para exibir as listas de documentos do paciente
     */
    private void exibirListaDados(String titulo, Object[] dados) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        if (dados.length == 0) {
            dialog.add(new JLabel("Nenhum registro encontrado.", SwingConstants.CENTER));
        } else {
            JList<Object> lista = new JList<>(dados);
            dialog.add(new JScrollPane(lista), BorderLayout.CENTER);
        }

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        dialog.add(btnFechar, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void criarRodape() {
        JPanel rodape = new JPanel();
        JButton btnLogout = new JButton("🚪 Sair do Sistema");

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginInterface(hospital); 
            }
        });

        rodape.add(btnLogout);
        add(rodape, BorderLayout.SOUTH);
    }
}
