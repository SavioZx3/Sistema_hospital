package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private List<Consulta> consultas;

    public Agenda() {
        this.consultas = new ArrayList<>();
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    // futuramente:
    // gerarConsultasSemana(...)
    // marcarConsulta(...)
    // cancelarConsulta(...)
}