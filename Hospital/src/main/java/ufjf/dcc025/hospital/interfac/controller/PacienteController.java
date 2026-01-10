package ufjf.dcc025.hospital.interfac.controller;

//import java.util.List;
//import java.util.ArrayList;

/**
 * Controller responsável pelas operações do paciente.
 *
 * Retorna dados simulados para exibição na interface gráfica.
 */
public class PacienteController {

    /**
     * Lista as consultas do paciente.
     */
    public String[][] listarConsultas() {
        return new String[][]{
                {"09:00", "Dr. Carlos"},
                {"14:30", "Dra. Ana"}
        };
    }

    /**
     * Lista os exames do paciente.
     */
    public String[][] listarExames() {
        return new String[][]{
                {"Exame de sangue", "10/01/2026"},
                {"Raio-X", "15/01/2026"}
        };
    }

    /**
     * Lista as receitas do paciente.
     */
    public String[][] listarReceitas() {
        return new String[][]{
                {"Dipirona", "3x ao dia"},
                {"Amoxicilina", "7 dias"}
        };
    }

    /**
     * Lista os atestados do paciente.
     */
    public String[][] listarAtestados() {
        return new String[][]{
                {"Atestado médico", "2 dias"}
        };
    }
}
