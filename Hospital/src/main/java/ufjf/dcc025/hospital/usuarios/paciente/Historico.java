package ufjf.dcc025.hospital.usuarios.paciente;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;

public class Historico {
    
    private final List<Consulta> consultas;
    private final List<Exame> exames;
    
    public Historico(){
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
    }
    
    public void adicionaConsulta(Consulta consulta) {
        if (consulta != null) {
            this.consultas.add(consulta);
        }
    }
    
    public void adicionaExame(Exame exame) {
        if (exame != null) {
            this.exames.add(exame);
        }
    }
    
    public List<Consulta> getConsultas(){
        return new ArrayList<>(this.consultas);   
    }
    
    public List<Exame> getExames(){
        return new ArrayList<>(this.exames); 
    }

    /**
     * Gera um relatório formatado para ser exibido na interface gráfica.
     * @return 
     */
    @Override
    public String toString() {
        if (consultas.isEmpty() && exames.isEmpty()) {
            return "Nenhum registro encontrado no histórico.";
        }

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append("=== HISTÓRICO CLÍNICO ===\n\n");

        if (!consultas.isEmpty()) {
            sb.append("--- CONSULTAS ---\n");
            for (Consulta c : consultas) {
                sb.append(String.format("[%s] Medico: %s | Status: %s\n", 
                    c.getData().format(dtf), 
                    c.getMedico().getNome(), 
                    c.getStatus()));
            }
            sb.append("\n");
        }

        if (!exames.isEmpty()) {
            sb.append("--- EXAMES ---\n");
            for (Exame e : exames) {
                sb.append(String.format("- %s\n", e.toString()));
            }
        }

        return sb.toString();
    }
}