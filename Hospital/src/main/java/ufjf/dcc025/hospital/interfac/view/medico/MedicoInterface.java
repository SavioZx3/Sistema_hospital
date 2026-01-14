package ufjf.dcc025.hospital.interfac.view.medico;

import javax.swing.*;
import java.awt.*;
import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.interfac.view.BaseView;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import java.util.ArrayList;
import java.util.List;

public class MedicoInterface extends BaseView {

    private final Medico medico;
    private AgendaMedicoView agendaView;

    public MedicoInterface(Hospital hospital, Medico medico) {
        super(hospital, "Módulo Médico");
        this.medico = medico;

        setLayout(new BorderLayout());

        add(criarTopoMedico(), BorderLayout.NORTH);
        criarAbas();
        criarRodape();           
        setVisible(true);

        exibirNotificacoesMedico();

    }

    private void exibirNotificacoesMedico() {
        List<String> avisos = medico.getAgenda().consumirNotificacoes();

        for (String aviso : avisos) {
            JOptionPane.showMessageDialog(
                this,                // componente pai (panel ou frame)
                aviso,               // mensagem
                "Notificação",       // título
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    // ======================= TOPO =======================

    private JPanel criarTopoMedico() {
        JPanel topo = criarTopo("Dr(a). " + medico.getNome());

        JLabel especialidade = new JLabel(
                "Especialidade: " + medico.getEspecialidade()
        );
        especialidade.setForeground(Color.WHITE);

        JButton btnConfigAgenda = new JButton("Configurar Agenda");
        btnConfigAgenda.addActionListener(e -> {
            ConfigurarAgendaView view =
                    new ConfigurarAgendaView(this, medico);
            view.setVisible(true);

            // sincroniza agenda após fechar
            agendaView.atualizar();
        });

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> executarLogout());

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        direita.setOpaque(false);
        direita.add(especialidade);
        direita.add(btnConfigAgenda);
        direita.add(btnLogout);

        topo.add(direita, BorderLayout.EAST);
        return topo;
    }

    // ======================= ABAS =======================

    private void criarAbas() {
        JTabbedPane abas = new JTabbedPane();

        VisitasMedicoView visitasView =
                new VisitasMedicoView(hospital, medico);

        agendaView = new AgendaMedicoView(medico, visitasView);

        abas.addTab("Consultas", agendaView);
        abas.addTab("Visitas / Internação", visitasView);

        add(abas, BorderLayout.CENTER);
    }


    // ======================= RODAPÉ =======================

    private void criarRodape() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBorder(BorderFactory.createEtchedBorder());
        footer.add(new JLabel("Status: Médico ativo"));
        add(footer, BorderLayout.SOUTH);
    }

    // ======================= LOGOUT =======================
}
