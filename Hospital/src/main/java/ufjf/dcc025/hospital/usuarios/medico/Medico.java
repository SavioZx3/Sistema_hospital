package ufjf.dcc025.hospital.usuarios.medico;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Agenda;
import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
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

    public void atualizarDisponibilidadeSemanal(
            List<DisponibilidadeDiaSemana> novaDisponibilidade) {

        validarDisponibilidadeSemanal(novaDisponibilidade);

        LocalDate proximaSegunda = calcularProximaSegunda();

        // Remove qualquer configuração que entraria em vigor na próxima semana
        disponibilidadeSemanal.removeIf(d ->
            d.getDataInicioVigencia().equals(proximaSegunda)
        );

        for (DisponibilidadeDiaSemana d : novaDisponibilidade) {
            d.definirDataInicioVigencia(proximaSegunda);
            disponibilidadeSemanal.add(d);
        }
    }

    public Agenda getAgenda(){
        return this.agenda;
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



    public List<DisponibilidadeDiaSemana> getDisponibilidadesProximaSemana() {
        LocalDate proximaSegunda = calcularProximaSegunda();

        return disponibilidadeSemanal.stream()
            .filter(d -> d.getDataInicioVigencia().equals(proximaSegunda))
            .collect(Collectors.toList());
    }

    private LocalDate calcularProximaSegunda() {
        return LocalDate.now()
            .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }







    
    public void definirAgenda(Agenda agenda){
        this.agenda1 = agenda;
    }

    public Agenda getAgenda1(){
        return agenda1;
    }

    /**
    * Adiciona uma consulta a agenda do medico.
    * @param consulta consulta a ser adicionada
    */  
    public void adicionarConsulta(Consulta consulta){
        //verifica se o medico está ativo antes de adicionar a consulta
        if(!ativo){
            throw new IllegalStateException("Médico está inativo, então não pode adicionar consultas.");
        }
        else{
            this.agenda.add(consulta);
        }
    }

    /**
    * Retorna a lista de consultas do medico.
    * @return lista de consultas
    */


    /**
     * retorna todos os historicos clinicos dos pacientes
     * @return lista de historicos
     */
    public List<HistoricoClinico> getHistoricos(){
        return new ArrayList<>(historicos);
    }
    /**
     * retorna a quantidade de consultas na agenda do medico
     * @return numero de consultas
     */
    public int quantidadeConsultas(){
        return agenda.size();
    }

    /**
     * verifica o medico possui uma consulta especifica
     * @param consulta a consulta a ser verificada
     * @return verdade se a consulta exisir, falso caso contrario
     */

    public boolean possuiConsulta(Consulta consulta){
        return agenda.contains(consulta);
    }


    /**
     * remove uma consulta da agenda do medico
     * @param consulta a consulta a ser removida
    */
    public void removerConsulta(Consulta consulta){
        if(!ativo){
            throw new IllegalStateException("Medico está inativo, então não pode remover consultas.");
        }
        agenda.remove(consulta);
    }

    public String getEspecialidade(){
        return especialidade;
    }

    /**
    * Verifica se o medico esta ativo no sistema.
    *
    * @return true se estiver ativo, false caso contrario
    */
    public boolean isAtivo(){
        return ativo;
    }
    
    /**
    * Desativa o medico no sistema.
    */
    public void desativar(){
        this.ativo = false;
    }

    /**
    * Ativa o medico no sistema.
    */
    public void ativar(){
        this.ativo=true;
    }

    /**
    * Retorna uma representacao em texto do medico.
    * @return string com os dados do medico
    */
   @Override
    public String toString() {
        return "Medico: " + nome + " | Especialidade: " + especialidade + " | Ativo: " + (ativo ? "Sim" : "Não");
    }

    /**
     * Emite um atestado medico para um paciente 
     *  @param paciente nome do paciente
     *  @param descricao descricao do atestado
     *  @return string que representa o atestado 
     */    
    public String emitirAtestado(String paciente, String descricao){
        return "Atestado Médico\nPaciente:" + paciente + "\nDescrição: " + descricao + "\nMedico: " + nome;
    }

     /**
      * Prescreve uma receita medica para um paciente 
      * @param paciente nome do paciente
      * @param medicamentos lista de medicamentos 
      * @return String representando a receita
      */
     public String preescreverReceitaString(String paciente, List<String> medicamentos){
        return "Receita Médica\nPaciente: " + paciente + "\nMedicamentos: " + String.join(", ", medicamentos) + "\nMedico: " + nome;
     }

     /**
      * Define um horario de atendimento do medico
      * @param horario horario de atendimento
      */
     public void adicionarHorarioAtendimento(HorarioAtendimento horario){
        horariosAtendimento.add(horario);
     }

     /**
      * retorna os horarios de atendimento do medico
      * @return lita de horarios
      */
     public List<HorarioAtendimento> getHorariosAtendimento(){
        return new ArrayList<>(horariosAtendimento);
     }

     /**
      * registra a falta de um paciente em uma consulta
      * @param consulta consulta em que o paciente faltou
      */
     public void registrarFaltaPaciente(Consulta consulta){
        if(!agenda.contains(consulta)){
            throw new IllegalArgumentException("Consulta não pertence à agenda do médico!");
        }
        consulta.registrarFalta();
     }

     /**
      * retorna as consultas em que o paciente faltou
      * @return lista de consultas com faltas
      */
     public List<Consulta> getConsultasComFaltas() {
        List<Consulta> faltas = new ArrayList<>();
        
        for (Consulta consulta : agenda) {
            if (!consulta.isCompareceu()) {
                faltas.add(consulta);
            }
        }
        return faltas;
    }

    @Override
    public String getTipo() {
        return "MEDICO";
    }
}