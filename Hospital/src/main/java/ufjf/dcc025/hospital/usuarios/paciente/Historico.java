package ufjf.dcc025.hospital.usuarios.paciente;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;

public class Historico {

    private final List<Consulta> consultas;
    private final List<Exame> exames;
    private final List<Atestado> atestados;
    private final List<Receita> receitas;

    public Historico() {
        this.consultas = new ArrayList<>();
        this.exames = new ArrayList<>();
        this.atestados = new ArrayList<>();
        this.receitas = new ArrayList<>();
    }

    // Métodos para Adicionar
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

    public void adicionaAtestado(Atestado atestado) {
        if (atestado != null) {
            this.atestados.add(atestado);
        }
    }

    public void adicionaReceita(Receita receita) {
        if (receita != null) {
            this.receitas.add(receita);
        }
    }

    // Getters retornando cópias para segurança
    public List<Consulta> getConsultas() {
        return new ArrayList<>(this.consultas);
    }

    public List<Exame> getExames() {
        return new ArrayList<>(this.exames);
    }

    public List<Atestado> getAtestados() {
        return new ArrayList<>(this.atestados);
    }

    public List<Receita> getReceitas() {
        return new ArrayList<>(this.receitas);
    }

    @Override
    public String toString() {
        if (consultas.isEmpty() && exames.isEmpty() && atestados.isEmpty() && receitas.isEmpty()) {
            return "Nenhum registro encontrado no histórico.";
        }

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append("=== HISTÓRICO CLÍNICO ===\n\n");

        // Adicionando as Consultas (Importante para ver quem atendeu e quando)
        if (!consultas.isEmpty()) {
            sb.append("--- CONSULTAS REALIZADAS ---\n");
            for (Consulta c : consultas) {
                sb.append(String.format("- %s | Médico: %s | Status: %s\n",
                        c.getData().format(dtf),
                        c.getMedico().getNome(),
                        c.getStatus()));
            }
            sb.append("\n");
        }

        if (!atestados.isEmpty()) {
            sb.append("--- ATESTADOS ---\n");
            for (Atestado a : atestados) {
                sb.append(String.format("- Data: %s | Dias: %d\n", a.getDataEmissao().format(dtf), a.getDiasAfastamento()));
            }
            sb.append("\n");
        }

        if (!receitas.isEmpty()) {
            sb.append("--- RECEITAS ---\n");
            for (Receita r : receitas) {
                sb.append(r.toString());
            }
        }

        return sb.toString();
    }

}
