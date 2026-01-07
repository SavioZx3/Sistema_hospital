package ufjf.dcc025.hospital.interfac.view;

import javax.swing.*;
import java.awt.*;

public class ControleVisitasView extends JPanel {

    public ControleVisitasView() {
        setLayout(new BorderLayout());

        String[] colunas = {"Paciente", "Status"};
        Object[][] dados = {
            {"Paciente A", "Apto"},
            {"Paciente B", "Não apto"},
            {"Paciente C", "Apto"}
        };

        JTable tabela = new JTable(dados, colunas);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }
}