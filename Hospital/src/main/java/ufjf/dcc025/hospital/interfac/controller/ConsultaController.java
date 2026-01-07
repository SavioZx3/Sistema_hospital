package ufjf.dcc025.hospital.interfac.controller;

/**
 * Controller responsável pelas operações relacionadas a consultas.
 *
 * Esta classe utiliza dados simulados (mock) e serve
 * como ponte entre as interfaces gráficas (Swing)
 * e as futuras regras de negócio.
 */
public class ConsultaController {

    /* ===================== */
    /* === PARTE MÉDICO === */
    /* ===================== */

    /**
     * Retorna a agenda do médico.
     * Dados simulados para exibição em tabela.
     *
     * @return matriz com paciente e horário
     */
    public String[][] listarAgendaMedico() {
        return new String[][]{
                {"Paciente A", "09:00"},
                {"Paciente B", "10:30"},
                {"Paciente C", "14:00"}
        };
    }

    /**
     * Retorna as consultas do médico.
     *
     * @return matriz com paciente e horário
     */
    public String[][] listarConsultasMedico() {
        return new String[][]{
                {"João", "09:00"},
                {"Maria", "10:30"}
        };
    }

    /* ========================= */
    /* === PARTE RECEPÇÃO === */
    /* ========================= */

    /**
     * Agenda uma nova consulta (mock).
     */
    public void agendarConsulta() {
        // aqui futuramente entraria lógica real
        System.out.println("Consulta agendada com sucesso! (mock)");
    }

    /**
     * Cancela uma consulta (mock).
     */
    public void cancelarConsulta() {
        // aqui futuramente entraria lógica real
        System.out.println("Consulta cancelada com sucesso! (mock)");
    }

    /**
     * Retorna as consultas do dia.
     *
     * @return matriz com horário e paciente
     */
    public String[][] listarConsultasDoDia() {
        return new String[][]{
                {"09:00", "João"},
                {"10:30", "Maria"}
        };
    }
}