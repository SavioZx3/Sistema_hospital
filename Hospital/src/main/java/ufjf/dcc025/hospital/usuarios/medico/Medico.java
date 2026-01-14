package ufjf.dcc025.hospital.usuarios.medico;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Agenda;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.Usuario;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;
import ufjf.dcc025.hospital.usuarios.paciente.StatusInternacao;
import ufjf.dcc025.hospital.usuarios.paciente.StatusVisita;

public class Medico extends Usuario {
    private Especialidade especialidade;
    private boolean ativo;
    private final Agenda agenda;
    private List<DisponibilidadeDiaSemana> disponibilidadeSemanal;

    public Medico(String nome, String cpf, String email, String senha, Especialidade especialidade) throws DadosInvalidosException {
        super(nome, cpf, email, senha);
        if (especialidade == null) {
            throw new DadosInvalidosException("A especialidade do medico não pode ser vazia");
        }
        this.especialidade = especialidade;
        this.ativo = true;
        this.agenda = new Agenda(this);
        this.disponibilidadeSemanal = new ArrayList<>();
    }

    // Getters e Setters
    @Override
    public String getTipo() { return "Médico"; }
    public Agenda getAgenda() { return this.agenda; }
    public Especialidade getEspecialidade() { return especialidade; }
    public boolean isAtivo() { return ativo; }
    public void desativar() { this.ativo = false; }
    public void ativar() { this.ativo = true; }

    // Disponibilidade
    public void atualizarDisponibilidadeSemanal(List<DisponibilidadeDiaSemana> novaDisponibilidade) {
        validarDisponibilidadeSemanal(novaDisponibilidade);
        LocalDate proximaVigencia = calcularProximaSegunda();

        List<DisponibilidadeDiaSemana> paraRemover = new ArrayList<>();
        for (DisponibilidadeDiaSemana d : disponibilidadeSemanal) {
            if (d.getDataInicioVigencia() != null && d.getDataInicioVigencia().equals(proximaVigencia)) {
                paraRemover.add(d);
            }
        }
        disponibilidadeSemanal.removeAll(paraRemover);

        for (DisponibilidadeDiaSemana d : novaDisponibilidade) {
            d.definirDataInicioVigencia(proximaVigencia);
            disponibilidadeSemanal.add(d);
        }
    }

    public List<DisponibilidadeDiaSemana> getDisponibilidadesProximaSemana() {
        LocalDate proximaSegunda = calcularProximaSegunda();
        List<DisponibilidadeDiaSemana> resultado = new ArrayList<>();
        for (DisponibilidadeDiaSemana d : disponibilidadeSemanal) {
            if (d.getDataInicioVigencia() != null && d.getDataInicioVigencia().equals(proximaSegunda)) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    private void validarDisponibilidadeSemanal(List<DisponibilidadeDiaSemana> lista) {
        if (lista == null || lista.isEmpty()) throw new IllegalArgumentException("Disponibilidade inválida");

        Set<DayOfWeek> diasUsados = new HashSet<>();
        Duration cargaHorariaTotal = Duration.ZERO;

        for (DisponibilidadeDiaSemana d : lista) {
            if (!diasUsados.add(d.getDiaSemana())) {
                throw new IllegalArgumentException("Dia duplicado: " + d.getDiaSemana());
            }

            if (!d.getHorarioInicio().isBefore(d.getHorarioFim())) {
                throw new IllegalArgumentException("Horário inválido em " + d.getDiaSemana());
            }

            Duration intervalo = Duration.between(d.getHorarioInicio(), d.getHorarioFim());
            if (intervalo.compareTo(d.getDuracaoConsulta()) < 0) {
                throw new IllegalArgumentException("Duração da consulta não cabe no horário em " + d.getDiaSemana());
            }
            cargaHorariaTotal = cargaHorariaTotal.plus(intervalo);
        }

        if (cargaHorariaTotal.compareTo(Duration.ofHours(8)) < 0) {
            throw new IllegalArgumentException("Carga horária semanal mínima de 8h não atingida");
        }
    }

    private LocalDate calcularProximaSegunda() {
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }

    // Ações Médicas
    public void realizarAtendimento(Consulta consulta, int diasAtestado, String remedio, String dose, LocalDate validade) {
        Paciente p = consulta.getPaciente();
        if (p == null) return;

        consulta.emitirAtestado(diasAtestado);
        consulta.emitirReceita();
        p.adicionarAtestado(consulta.getAtestado());
        p.adicionarReceita(consulta.getReceita());
        consulta.marcarConcluida();
    }

    @Override
    public String toString() {
        return " Dr(a). " + nome + " (" + especialidade + ")";
    }
}