package ufjf.dcc025.hospital.interfac.controller;

import java.time.LocalDate;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class MedicoController {

    public void realizarAtendimentoCompleto(Medico medico, Consulta consulta, 
                                          int diasAtestado, String remedio, 
                                          String dose, LocalDate validade) {
        
        // Chama o método da classe médico
        medico.realizarAtendimento(consulta, diasAtestado, remedio, dose, validade);
        
        // Aqui, futuramente, a persistência salvaria o estado atualizado do paciente
    }
}
