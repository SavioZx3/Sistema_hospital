
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
    private String especialidade;
    private boolean ativo;
    private final Agenda agenda;
    private List<DisponibilidadeDiaSemana> disponibilidadeSemanal;

    public Medico(String nome, String cpf, String email, String senha, String especialidade) throws DadosInvalidosException {
        super(nome, cpf, email, senha);
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new DadosInvalidosException("A especialidade do medico não pode ser vazia");
        }
        this.especialidade = especialidade;
        this.ativo = true;
        this.agenda = new Agenda(this);
        this.disponibilidadeSemanal = new ArrayList<>();
    }

    public void atualizarDisponibilidadeSemanal(List<DisponibilidadeDiaSemana> novaDisponibilidade) {
        validarDisponibilidadeSemanal(novaDisponibilidade);
        LocalDate proximaSegunda = calcularProximaSegunda();

        List<DisponibilidadeDiaSemana> paraRemover = new ArrayList<>();
        for (DisponibilidadeDiaSemana d : disponibilidadeSemanal) {
            if (d.getDataInicioVigencia() != null && d.getDataInicioVigencia().equals(proximaSegunda)) {
                paraRemover.add(d);
            }
        }
        disponibilidadeSemanal.removeAll(paraRemover);

        for (DisponibilidadeDiaSemana d : novaDisponibilidade) {
            d.definirDataInicioVigencia(proximaSegunda);
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
            cargaHorariaTotal = cargaHorariaTotal.plus(d.getCargaHorariaDia());
        }
        
        if (cargaHorariaTotal.compareTo(Duration.ofHours(8)) < 0) {
            throw new IllegalArgumentException("Carga horária semanal mínima de 8h não atingida");
        }
    }

    private LocalDate calcularProximaSegunda() {
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }

    public void internarPaciente(Paciente paciente, StatusVisita permissaoVisita) {
        if (paciente != null) {
            paciente.setStatusInternacao(StatusInternacao.INTERNADO);
            paciente.setStatusVisita(permissaoVisita);
        }
    }

    public void darAlta(Paciente paciente) {
        if (paciente != null) {
            paciente.setStatusInternacao(StatusInternacao.NAO_INTERNADO);
            paciente.setStatusVisita(StatusVisita.APTO);
        }
    }

    public void realizarAtendimento(Consulta consulta, int diasAtestado, String remedio, String dose, LocalDate validade) {
        Paciente p = consulta.getPaciente();
        if (p == null) return;
        
        // Emite os documentos na consulta
        consulta.emitirAtestado(diasAtestado);
        consulta.emitirReceita(remedio, dose, validade);
        
        // Adiciona fisicamente às listas do paciente para o requisito de "Acesso Digital"
        p.adicionarAtestado(consulta.getAtestado());
        p.adicionarReceita(consulta.getReceita());
        
        consulta.marcarConcluida();
    }

    // --- GETTERS E SETTERS ---

    public Agenda getAgenda() { return this.agenda; }
    public String getEspecialidade() { return especialidade; }
    public boolean isAtivo() { return ativo; }
    public void desativar() { this.ativo = false; }
    public void ativar() { this.ativo = true; }

    @Override
    public String getTipo() { return "MEDICO"; }

    @Override
    public String toString() {
        return "Dr(a). " + nome + " (" + especialidade + ")";
    }
}
