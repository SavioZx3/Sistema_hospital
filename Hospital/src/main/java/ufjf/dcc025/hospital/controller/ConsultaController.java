package ufjf.dcc025.hospital.controller;

/**
 * Controller responsável pelas operações relacionadas a consultas.
 * 
 * Atualmente utiliza dados simulados (mock) apenas para testes da interface.
 */
public class ConsultaController {

    /* ===================== */
    /* === PARTE MÉDICO === */
    /* ===================== */

    /**
     * Lista a agenda do médico (mock).
     */
    public void listarAgendaMedico() {
        System.out.println("\n=== AGENDA DO MÉDICO ===");
        System.out.println("09:00 - Consulta com paciente A");
        System.out.println("10:30 - Consulta com paciente B");
        System.out.println("14:00 - Consulta com paciente C");
        System.out.println("(dados simulados)");
    }

    /**
     * Lista as consultas do médico (mock).
     */
    public void listarConsultas() {
        System.out.println("\n=== CONSULTAS ===");
        System.out.println("Paciente: João | Horário: 09:00");
        System.out.println("Paciente: Maria | Horário: 10:30");
        System.out.println("(dados simulados)");
    }

    /* === PARTE RECEPÇÃO === */

    /**
     * Agenda uma nova consulta (mock).
     */
    public void agendarConsulta() {
        System.out.println("\nAgendamento de consulta (mock)");
        System.out.println("Consulta agendada com sucesso!");
    }

    /**
     * Cancela uma consulta (mock).
     */
    public void cancelarConsulta() {
        System.out.println("\nCancelamento de consulta (mock)");
        System.out.println("Consulta cancelada com sucesso!");
    }

    /**
     * Lista as consultas do dia (mock).
     */
    public void listarConsultasDoDia() {
        System.out.println("\n=== CONSULTAS DO DIA ===");
        System.out.println("09:00 - João");
        System.out.println("10:30 - Maria");
        System.out.println("(dados simulados)");
    }
}