
package ufjf.dcc025.hospital.interfac.controller;

/**
 * Controller responsável pela autenticação do usuário.
 * 
 * Atualmente utiliza autenticação simulada baseada no email.
 */
public class LoginController {

    /**
     * Tipos de usuários do sistema.
     */
    public enum TipoUsuario {
        PACIENTE,
        MEDICO,
        RECEPCIONISTA
    }

    /**
     * Autentica o usuário com base no email (mock).
     * 
     * @param email email informado no login
     * @return tipo de usuário identificado
     */
    public TipoUsuario autenticar(String email) {

        if (email.contains("medico")) {
            return TipoUsuario.MEDICO;
        }

        if (email.contains("recep")) {
            return TipoUsuario.RECEPCIONISTA;
        }

        return TipoUsuario.PACIENTE;
    }
}