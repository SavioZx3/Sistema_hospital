package ufjf.dcc025.hospital.usuarios.medico;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Agenda;
import ufjf.dcc025.hospital.usuarios.Usuario;

/**
 * @author mjjun
 */


/**
 * Representa um medico do sistema hospitalar.
 * O medico é um tipo de usuario que possui uma especialidade,
 * um status de atividade e uma agenda de consultas.
 */
public class Medico extends Usuario{
    private String especialidade;
    private boolean ativo;
    private final Agenda agenda;

    private List<DisponibilidadeDiaSemana> disponibilidadeSemanal;

    /**
    * Cria um medico com nome, CPF e especialidade.
    * @param nome nome do medico
     * @param cpf
     * @param email
    * @param senha senha do medico
    * @param especialidade especialidade medica
    */
    public Medico(String nome , String cpf, String email, String senha, String especialidade) throws DadosInvalidosException{
        super(nome, cpf, email, senha);
        if(especialidade == null || especialidade.trim().isEmpty()){
            throw new DadosInvalidosException("A especialidade do medico não pode ser vazia");
        }
        this.especialidade = especialidade;
        this.ativo = true;
        this.agenda = new Agenda(this);
        this.disponibilidadeSemanal = new ArrayList<>();
    }

    //============================ Getters e Setters ==========================//

      @Override
    public String getTipo() {
        return "Médico";
    }

    public Agenda getAgenda(){
        return this.agenda;
    }

    public String getEspecialidade(){
        return especialidade;
    }

    public List<DisponibilidadeDiaSemana> getDisponibilidadesDaquiDuasSemanas() {
        LocalDate segundaMaisDuasSemanas = calcularSegundaMaisDuasSemanas();

        return disponibilidadeSemanal.stream()
            .filter(d -> d.getDataInicioVigencia().equals(segundaMaisDuasSemanas))
            .collect(Collectors.toList());
    }

    public boolean isAtivo(){
        return ativo;
    }
    
    public void desativar(){
        this.ativo = false;
    }

    public void ativar(){
        this.ativo=true;
    }


    //=========================== Disponibilidade Semanal =========================//

    public void atualizarDisponibilidadeSemanal(
            List<DisponibilidadeDiaSemana> novaDisponibilidade) {

        validarDisponibilidadeSemanal(novaDisponibilidade);

        LocalDate segundaMaisDuasSemanas = calcularSegundaMaisDuasSemanas();

        // Remove qualquer configuração que entraria em vigor na próxima semana
        disponibilidadeSemanal.removeIf(d ->
            d.getDataInicioVigencia().equals(segundaMaisDuasSemanas)
        );

        for (DisponibilidadeDiaSemana d : novaDisponibilidade) {
            d.definirDataInicioVigencia(segundaMaisDuasSemanas);
            disponibilidadeSemanal.add(d);
        }
    }

    private void validarDisponibilidadeSemanal(List<DisponibilidadeDiaSemana> lista) {

        if (lista == null || lista.isEmpty())
            throw new IllegalArgumentException("Disponibilidade inválida");

        Set<DayOfWeek> diasUsados = new HashSet<>();
        Duration cargaHorariaTotal = Duration.ZERO;

        for (DisponibilidadeDiaSemana d : lista) {

            if (!diasUsados.add(d.getDiaSemana()))
                throw new IllegalArgumentException(
                    "Dia da semana duplicado: " + d.getDiaSemana()
                );

            if (!d.getHorarioInicio().isBefore(d.getHorarioFim()))
                throw new IllegalArgumentException(
                    "Horário inválido em " + d.getDiaSemana()
                );

            Duration intervalo = Duration.between(
                d.getHorarioInicio(),
                d.getHorarioFim()
            );

            if (intervalo.isNegative() || intervalo.isZero())
                throw new IllegalArgumentException(
                    "Intervalo inválido em " + d.getDiaSemana()
                );

            if (d.getDuracaoConsulta().isZero() ||
                d.getDuracaoConsulta().isNegative())
                throw new IllegalArgumentException(
                    "Duração de consulta inválida em " + d.getDiaSemana()
                );

            if (intervalo.compareTo(d.getDuracaoConsulta()) < 0)
                throw new IllegalArgumentException(
                    "Duração da consulta não cabe no horário em " + d.getDiaSemana()
                );

            cargaHorariaTotal = cargaHorariaTotal.plus(intervalo);
        }

        if (cargaHorariaTotal.compareTo(Duration.ofHours(8)) < 0)
            throw new IllegalArgumentException(
                "Carga horária semanal mínima de 8h não atingida"
            );
    }

    private LocalDate calcularSegundaMaisDuasSemanas() {
        return LocalDate.now()
            .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            .plusWeeks(1);
    }

   @Override
    public String toString() {
        return "Medico: " + nome + " | Especialidade: " + especialidade + " | Ativo: " + (ativo ? "Sim" : "Não");
    }
}