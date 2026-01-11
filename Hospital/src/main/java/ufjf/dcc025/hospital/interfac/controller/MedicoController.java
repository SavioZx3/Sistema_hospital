package ufjf.dcc025.hospital.interfac.controller;

/**
 * Controller responsável pelas ações realizadas pelo médico.
 *
 * Esta classe fornece dados simulados (mock) para
 * alimentar as interfaces gráficas (Swing).
 */
public class MedicoController {

    /**
     * Retorna os atestados emitidos pelo médico.
     *
     * @return matriz com CPF do paciente, dias e observação
     */
    public String[][] listarAtestados() {
        return new String[][]{
                {"123.456.789-00", "2", "Repouso recomendado"},
                {"987.654.321-00", "5", "Pós-operatório"}
        };
    }

    /**
     * Retorna os exames solicitados pelo médico.
     *
     * @return matriz com CPF, exame e data
     */
    public String[][] listarExamesSolicitados() {
        return new String[][]{
                {"123.456.789-00", "Exame de sangue", "10/01/2026"},
                {"987.654.321-00", "Raio-X", "15/01/2026"}
        };
    }

    /**
     * Retorna as receitas emitidas pelo médico.
     *
     * @return matriz com CPF, medicamento e posologia
     */
    public String[][] listarReceitas() {
        return new String[][]{
                {"123.456.789-00", "Dipirona", "3x ao dia"},
                {"987.654.321-00", "Amoxicilina", "7 dias"}
        };
    }

    /* ===== AÇÕES (mock) ===== */

    public void emitirAtestado(String cpf, int dias, String mensagem) {
        // futuramente conecta com model
    }

    public void solicitarExame(String cpf, String exame, String data) {
        // futuramente conecta com model
    }

    public void emitirReceita(String cpf, String medicamento, String posologia) {
        // futuramente conecta com model
    }
}