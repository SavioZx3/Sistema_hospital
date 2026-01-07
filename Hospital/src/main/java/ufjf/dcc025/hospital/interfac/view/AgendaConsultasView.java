package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class AgendaConsultasView extends JPanel {

    public AgendaConsultasView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Dia", "Horário", "Médico"};
        Object[][] dados = {
            {"Segunda", "14h", "Dr. João"},
            {"Terça", "15h", "Dra. Ana"},
            {"Quarta", "16h", "Dr. Carlos"}
        };

        JTable tabela = new JTable(dados, colunas);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }
}