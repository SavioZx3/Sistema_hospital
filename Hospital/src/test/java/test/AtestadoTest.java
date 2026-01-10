package test;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Atestado;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class AtestadoTest {
    
    private Medico medico;
    private Paciente paciente;
    private Historico h;
    private String msgPadrao;
    private LocalDate dataPadrao;
    private int duracaoPadrao;
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws DadosInvalidosException {
        // Limpa os registros estáticos para que cada teste seja independente
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();

        h = new Historico();
       
        medico = new Medico("Dr. Estranho", "888.888.888-88", "estranho@marvel.com", "senha123", "Neurocirurgião");
        paciente = new Paciente("Parker", "111.111.111-11", "peter@marvel.com", "senha123", h, "Queens", "32 98888-0000");
        
        msgPadrao = "O paciente esta mal merece ficar em casa por pelo menos 2 semanas ass: " + medico.getNome();
        dataPadrao = LocalDate.now();
        duracaoPadrao = 14;
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void atestadoValido() throws Exception {
        Atestado a = new Atestado(medico, paciente, msgPadrao, dataPadrao, duracaoPadrao);
        Assertions.assertEquals(duracaoPadrao, a.getDuracaoDias());
        Assertions.assertEquals(dataPadrao, a.getDataEmissao());
        Assertions.assertEquals(medico, a.getMedicoResponsavel());
        Assertions.assertEquals(msgPadrao, a.getMensagem());
        Assertions.assertEquals(paciente, a.getPacienteAlvo());
    }
    
    @Test
    public void testDuracaoInvalida() {
        try {
            new Atestado(medico, paciente, msgPadrao, dataPadrao, 0);
            Assertions.fail("Deveria falhar pois a duração é 0 ");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().contains("no minimo 1 dia"));
        } catch (Exception e) {
            Assertions.fail("Erro inesperado: " + e.getMessage());
        }
    }
    
    @Test 
    public void testMensagemInvalida() {
        try {
            new Atestado(medico, paciente, "", dataPadrao, duracaoPadrao);
            Assertions.fail("Deveria falhar pois a mensagem esta vazia ");
        } catch (DadosInvalidosException e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testAlterarMensagemEmitida() {
        Atestado a = new Atestado(medico, paciente, msgPadrao, dataPadrao, duracaoPadrao);
        a.emitir();
        
        try {
            a.alterarMensagem("nova msg de teste");
            Assertions.fail("Deve falhar pois não deveria permitir alterar a mensagem após a emissão");
        } catch (IllegalStateException e) {
            Assertions.assertEquals("Atestado já emitido e não pode ser alterado.", e.getMessage());
        }
    }
    
    @Test
    public void testAlterarMensagemNaoEmitida() {
        String novaMensagem = "teste";
        Atestado a = new Atestado(medico, paciente, msgPadrao, dataPadrao, duracaoPadrao);
        a.alterarMensagem(novaMensagem);
        Assertions.assertEquals(novaMensagem, a.getMensagem());
    }
    
    @Test
    public void testAlterandoMensagemParaVazia() {
        Atestado a = new Atestado(medico, paciente, msgPadrao, dataPadrao, duracaoPadrao);
        try {
            a.alterarMensagem(" ");
            Assertions.fail("Deve falhar pois a mensagem não pode ser vazia");
        } catch (DadosInvalidosException e) {
            Assertions.assertEquals("Nova mensagem não deve ser vazia!", e.getMessage());
        }
    }
}