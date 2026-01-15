package ufjf.dcc025.hospital.persistencia;

/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */

import java.util.ArrayList;
import java.util.List;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Recepcionista;
import ufjf.dcc025.hospital.usuarios.medico.Especialidade;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class HospitalData {

    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Recepcionista> recepcionistas;
    private List<Consulta> consultas = new ArrayList<>();
    private List<String> emailsCadastrados;

    public HospitalData() {
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        recepcionistas = new ArrayList<>();
        consultas = new ArrayList<>();
        emailsCadastrados = new ArrayList<>();
    }

    public static HospitalData criarComDadosDefault() {

        HospitalData dados = new HospitalData();
        try {
            //Criando médico padrão
            Medico medicoDefault = new Medico("Adm Médico", "000.000.000-00", "med@hospital.com", "1234", Especialidade.CLINICA_GERAL);

            //Criando paciente padrão
            Paciente pacienteDefault = new Paciente("Adm Paciente", "111.111.111-11", "pac@hospital.com", "1234", "(32) 9 8888-0000", "Juiz de fora", "São pedro", "Rua a", "27");

            //Criando Recepcionista padrão
            Recepcionista recepcionistaPadrao = new Recepcionista("Adm Recepcionista", "222.222.222-22", "recep@hospital.com", "1234");

            dados.getMedicos().add(medicoDefault);
            dados.getPacientes().add(pacienteDefault);
            dados.getRecepcionistas().add(recepcionistaPadrao);
        } catch (DadosInvalidosException e) {
            System.err.println("Erro ao criar dados iniciais: " + e.getMessage());
        }

        return dados;

    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(List<Medico> medicos) {
        this.medicos = medicos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<Recepcionista> getRecepcionistas() {
        return recepcionistas;
    }

    public void setRecepcionistas(List<Recepcionista> recepcionistas) {
        this.recepcionistas = recepcionistas;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public List<String> getEmailsCadastrados() {
        return emailsCadastrados;
    }

    public void setEmailsCadastrados(List<String> emails) {
        this.emailsCadastrados = emails != null ? new ArrayList<>(emails) : new ArrayList<>();
        // Reconstruir HashSet estático do Email
        Email.limparEmailsCadastrados();
        if (emails != null) {
            for (String e : emails) {
                Email.adicionarEmailStatic(e); 
            }
        }
    }
}
