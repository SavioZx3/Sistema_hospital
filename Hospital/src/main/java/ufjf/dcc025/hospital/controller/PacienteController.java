package ufjf.dcc025.hospital.controller;

import java.util.List;
import java.util.ArrayList;

public class PacienteController {
    
    public List<String> listarConsultas(){
        List<String> consultas = new ArrayList<>();
        consultas.add("09:00 - Consulta com Dr.Carlos");
        consultas.add("14:30 - Consulta com Dra.Ana");
        return consultas;
    }

    public List<String> listarExames(){
        List<String> exames = new ArrayList<>();
        exames.add("Exame de sangue - 10/01/2026");
        exames.add("Raio-X - 15/01/2026");
        return exames;
    }

    public List<String> listarReceitas(){
        List<String> receitas = new ArrayList<>();
        receitas.add("Dipirona - 3x ao dia");
        receitas.add("Amoxicilina - 7 dias");
        return receitas;
    }

    public List<String> listarAtestados(){
        List<String> atestados = new ArrayList<>();
        atestados.add("Atestado Médico - 2 dias");
        return atestados;
    }

}
