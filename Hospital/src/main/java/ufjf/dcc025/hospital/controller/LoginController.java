package ufjf.dcc025.hospital.controller;

public class LoginController {

    public enum TipoUsuario{
        PACIENTE, MEDICO, RECEPCIONISTA
    }

    ///MOCK de autentificação por email (ideia simples por enquanto)
    public TipoUsuario autenticar(String email){
        if(email.contains("medico")){
            return TipoUsuario.MEDICO;
        }
        if(email.contains("recep")){
            return TipoUsuario.RECEPCIONISTA;
        }

        return TipoUsuario.PACIENTE;
    }
}
