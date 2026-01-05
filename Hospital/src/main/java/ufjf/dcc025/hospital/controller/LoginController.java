package ufjf.dcc025.hospital.controller;

public class LoginController {

    public enum TipoUsuario{
        PACIENTE, MEDICO, RECEPCIONISTA
    }

    public TipoUsuario escolheTipoUsuario(int opcao){
        return switch(opcao){
            case 1 -> TipoUsuario.PACIENTE;
            case 2 -> TipoUsuario.MEDICO;
            case 3 -> TipoUsuario.RECEPCIONISTA;
            default -> null;
        };
    }
}
