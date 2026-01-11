package ufjf.dcc025.hospital.persistencia;

import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;


public class HospitalData {

    
    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Recepcionista> recepcionistas;
    
    public HospitalData(){
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        recepcionistas = new ArrayList<>();
    }
    
    static HospitalData criarComDadosDefault() {
        
        HospitalData dados = new HospitalData();
        try{
            //Criando médico padrão
            Medico medicoDefault = new Medico("Adm Médico", "000.000.000-00", "med@hospital.com", "1234", "Clinico Geral");
            
            //Criando paciente padrão
            Paciente pacienteDefault = new Paciente("Adm Paciente", "111.111.111-11", "pac@hospital.com", "1234", "(32) 9 8888-0000", "Juiz de fora", "São pedro", "Rua a", "27");
            
            //Criando Recepcionista padrão
            Recepcionista recepcionistaPadrao = new Recepcionista("Adm Recepcionista", "222.222.222-22", "recep@hospital.com", "1234");
            
            dados.getMedico().add(medicoDefault);
            dados.getPaciente().add(pacienteDefault);
            dados.getRecepcionista().add(recepcionistaPadrao);
        } catch(DadosInvalidosException e){
            System.err.println("Erro ao criar dados iniciais: " + e.getMessage());
        }
        
        return dados;
        
    }
    
    public List<Medico> getMedico(){
        return medicos;
    }
    
    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }
    
    public List<Paciente> getPaciente(){
        return pacientes;
    }
    
    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }
    
    public List<Recepcionista> getRecepcionista(){
        return recepcionistas;
    }
    
    public void setRecepcionistas(List<Recepcionista> recepcionistas) {
        this.recepcionistas = recepcionistas;
    }
    
}
