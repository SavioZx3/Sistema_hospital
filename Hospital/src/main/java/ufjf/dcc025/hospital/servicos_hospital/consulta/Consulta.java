package ufjf.dcc025.hospital.servicos_hospital.consulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Exame;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class Consulta {

    private final LocalDate data;
    private final LocalTime horarioInicio;
    private final LocalTime horarioFim;
    private final Medico medico;
    private Paciente paciente;

    private Atestado atestado;
    private Receita receita;
    private Exame exame;

    private StatusConsulta status;

    public Consulta(LocalDate data, LocalTime horarioInicio, LocalTime horarioFim, Medico medico) throws DadosInvalidosException, DataInvalidaException {
        if(medico == null) throw new DadosInvalidosException("O medico responsável deve ser informado!");
        if(!medico.isAtivo()) throw new DadosInvalidosException("O médico está inativo.");
        if(data == null || data.isBefore(LocalDate.now())) throw new DataInvalidaException("Data inválida.");
        if (horarioInicio == null || horarioFim == null) throw new DadosInvalidosException("Horários inválidos.");
        if (!horarioFim.isAfter(horarioInicio)) throw new DadosInvalidosException("Fim deve ser após o início.");

        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.medico = medico;
        this.paciente = null;
        this.status = StatusConsulta.DISPONIVEL;
    }

    // ================= Getters (Restaurados para compatibilidade) ================= //
    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }
    public LocalDate getData() { return data; }
    public StatusConsulta getStatus() { return status; }
    public Atestado getAtestado() { return atestado; }
    public Receita getReceita() { return receita; }
    public Exame getExame() { return exame; }

    public LocalDateTime getDataHoraInicio() { return horarioInicio.atDate(data); }
    public LocalDateTime getDataHoraFim() { return horarioFim.atDate(data); }

    // ================= Métodos de Status (Public) ================= //
    public boolean isDisponivel() { return this.status == StatusConsulta.DISPONIVEL; }
    public boolean isAgendada() { return this.status == StatusConsulta.AGENDADA; }
    public boolean isEmAndamento() { return this.status == StatusConsulta.EM_ANDAMENTO; }
    public boolean isRealizada() { return this.status == StatusConsulta.REALIZADA; }

    public void agendar(Paciente paciente) throws DadosInvalidosException, IllegalStateException {
        if(!isDisponivel()) throw new IllegalStateException("Consulta não disponível.");
        if(paciente == null) throw new DadosInvalidosException("Paciente inválido.");
        
        this.paciente = paciente;
        this.status = StatusConsulta.AGENDADA;
        this.medico.getAgenda().notificarAgendamento();
    }

    public void marcarInicioConsulta() throws IllegalStateException {
        if(!isAgendada()) throw new IllegalStateException("A consulta precisa estar agendada.");

        // TRAVA DE HORÁRIO REMOVIDA para permitir início antecipado como solicitado
        // if(LocalDateTime.now().isBefore(getDataHoraInicio())) ...

        if(LocalDateTime.now().isAfter(getDataHoraFim()))
            throw new IllegalStateException("Não é possível iniciar após o horário de término.");

        this.status = StatusConsulta.EM_ANDAMENTO;
    }

    public void marcarConcluida() throws IllegalStateException {
        if(!isEmAndamento()) throw new IllegalStateException("Consulta não está em andamento.");
        this.status = StatusConsulta.REALIZADA;
    }

    public void cancelarConsulta() throws IllegalStateException {
        if(!isAgendada()) throw new IllegalStateException("Só cancela se estiver agendada.");
        this.paciente = null;
        this.status = StatusConsulta.DISPONIVEL;
        this.medico.getAgenda().notificarCancelamento();
    }

    public void registrarFalta() throws IllegalStateException {
        if(!isAgendada()) throw new IllegalStateException("Só registra falta se agendada.");
        this.status = StatusConsulta.FALTA;
        this.medico.getAgenda().notificarFalta();
    }

    // ================= Emissão de Documentos ================= //
    public void getAtestado(int duracaoDias) throws IllegalStateException, DadosInvalidosException, DataInvalidaException {
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Status inválido para emitir atestado.");
        this.atestado = new Atestado(medico, paciente, LocalDate.now(), duracaoDias);
    }

    public void getReceita(String medicamento, String quantidade, LocalDate validade) throws IllegalStateException, DadosInvalidosException, DataInvalidaException {
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Status inválido para emitir receita.");
        this.receita = new Receita(medicamento, quantidade, paciente, medico, LocalDate.now(), validade);
    }

    public void emitirExame(String tipoExame, String resultado) throws IllegalStateException {
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Status inválido para solicitar exame.");
        this.exame = new Exame(tipoExame, LocalDate.now(), medico, paciente, resultado);
    }
   
    public void setExame(String descricao) throws IllegalStateException {
        if(!isRealizada() && !isEmAndamento())
            throw new IllegalStateException("Status inválido para solicitar exame.");

        // Usamos o seu método existente emitirExame
        // O primeiro parâmetro é o tipo/descrição, o segundo é o resultado (vazio no início)
        this.emitirExame(descricao, "AGUARDANDO RESULTADO");
    }
}