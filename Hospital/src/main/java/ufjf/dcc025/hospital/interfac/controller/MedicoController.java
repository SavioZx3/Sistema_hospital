package ufjf.dcc025.hospital.interfac.controller;

import ufjf.dcc025.hospital.Hospital;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;

public class MedicoController {

    private final Hospital hospital;

    public MedicoController(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * O controller agora apenas coordena a finalização do atendimento
     * delegando para o médico e garantindo a persistência.
     */
    public void realizarAtendimentoCompleto(Medico medico, Consulta consulta) {
        
        // 1. O médico processa o que foi gerado na consulta e move para o histórico do paciente
        medico.realizarAtendimento(consulta, hospital);
        
    }
}