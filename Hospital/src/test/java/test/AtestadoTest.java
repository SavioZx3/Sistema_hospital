package test;
/*
        Mateus Gomes da Silva Matrícula: 202465565C 
        Marcos José De Oliveira Júnior Matrícula: 202135011 
        Sávio Machado Lima Matrícula: 202435014
 */
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.usuarios.medico.Especialidade;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class AtestadoTest {
    
    private Medico medico;
    private Paciente paciente;
    private String msgPadrao;
    private LocalDate dataPadrao;
    private int duracaoPadrao;
    
    
    @BeforeEach
    public void setUp() throws DadosInvalidosException {
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();

        this.medico = criarMedicoPadrao();
        this.paciente = criarPacientePadrao();
        
        this.msgPadrao = "O paciente está mal e merece repouso. Ass: " + medico.getNome();
        this.dataPadrao = LocalDate.now();
        this.duracaoPadrao = 14;
    }


    private Medico criarMedicoPadrao() throws DadosInvalidosException {
        return new Medico("Dr. Estranho", "888.888.888-88", "estranho@marvel.com", "senha123", Especialidade.CARDIOLOGIA);
    }

    private Paciente criarPacientePadrao() throws DadosInvalidosException {
        return new Paciente("Peter Parker", "111.111.111-11", "peter@marvel.com", "senha123", "21 99999-0000",  "Nova York", "Queens", "Ingram Street", "20"
        );
    }

    @Test
    public void atestadoValido() throws Exception {
        Atestado a = new Atestado(medico, paciente, dataPadrao, duracaoPadrao);
        Assertions.assertEquals(duracaoPadrao, a.getDiasAfastamento());
        Assertions.assertEquals(medico, a.getMedico());
        Assertions.assertEquals(paciente, a.getPaciente());
    }
    
    @Test
    public void testDuracaoInvalida() {
        try {
            new Atestado(medico, paciente, dataPadrao, 0);
            Assertions.fail("Deveria falhar pois a duração é 0");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().contains("no minimo 1 dia"));
        } catch (Exception e) {
            Assertions.fail("Erro inesperado: " + e.getMessage());
        }
    }
}