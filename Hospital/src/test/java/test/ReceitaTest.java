package test;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.servicos_hospital.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;


public class ReceitaTest {
    
    private Medico medico;
    private Paciente paciente;
    private Historico h;
    
    private String medPadrao;
    private String qtdPadrao;
    private LocalDate emissaoPadrao;
    private LocalDate validadePadrao;
    
    @BeforeAll
    public static void setUpClass() throws Exception {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        
        h = new Historico();
        medico = new Medico("Dr. Octopus", "888.888.888-88" , "login", "Senha", "Físico nuclear");
        paciente = new Paciente("Peter", "111.111.111-11" , "login", "senha", h, "rua b", "32 98888-0000");
        medPadrao = "Dipirona";
        qtdPadrao = "500mg";
        emissaoPadrao = LocalDate.now();
        validadePadrao = LocalDate.now().plusDays(7);
    }
    
    @AfterEach
    public void tearDown() {
    }

    
     @Test
     public void testReceitaValida(){
        
        Receita receita = new Receita(medPadrao, qtdPadrao, paciente, medico, emissaoPadrao, validadePadrao);
            
        Assertions.assertNotNull(receita);
        Assertions.assertEquals("Dipirona", receita.getMedicamento());
        Assertions.assertEquals(emissaoPadrao, receita.getDataEmissao());
        Assertions.assertEquals(validadePadrao, receita.getValidade()); 
     }
     
    @Test
    public void testReceitaComDataValidadeInvalida() {
        
        LocalDate dataEmissao = LocalDate.of(2025, 1, 2);
        LocalDate dataValidade = LocalDate.of(2025, 1, 1);
        
        try {
             new Receita(medPadrao, qtdPadrao, paciente, medico, dataEmissao, dataValidade);
             Assertions.fail("Deveria lançar a exceção DataInvalidaException: validade anterior à emissão ");
             
        } catch (DataInvalidaException e) {
            Assertions.assertTrue(e.getMessage().contains("anterior"));
        } catch (DadosInvalidosException e){
            Assertions.fail("Lançou a exceção errada (DadosInvalidos)");
        }
    }
    
    @Test
    public void testMedicamentoVazio(){
        
        String medVazio = " ";
        try {
            new Receita(medVazio, qtdPadrao, paciente, medico,emissaoPadrao, validadePadrao);
            Assertions.fail("Deveria ter lançado DadosInvalidosException para medicamento vazio");
            
        } catch (DadosInvalidosException e) {
            Assertions.assertNotNull(e.getMessage());
        }   catch (DataInvalidaException e) {
            Assertions.fail("Lançou erro de data, mas o erro esperado era de dados");
        } 
    }
    
    @Test
    public void testQuantidadeVazia(){
        
        String qtdVazia = " ";
        try {
            new Receita(medPadrao, qtdVazia, paciente, medico,emissaoPadrao, validadePadrao);
            Assertions.fail("Deveria ter lançado DadosInvalidosException para quantidade vazia");
            
        } catch (DadosInvalidosException e) {
            Assertions.assertNotNull(e.getMessage());
        }   catch (DataInvalidaException e) {
            Assertions.fail("Lançou erro de data, mas o erro esperado era de dados");
        } 
    }
    
    @Test
    public void testImpressaoToString(){
        
        Receita r = new Receita(medPadrao, qtdPadrao, paciente, medico, emissaoPadrao, validadePadrao);
        String texto = r.toString();
        
        
        Assertions.assertTrue(texto.contains("RECEITA MÉDICA"));
        Assertions.assertTrue(texto.contains(medPadrao));
        Assertions.assertTrue(texto.contains(qtdPadrao));
        Assertions.assertTrue(texto.contains(medico.getNome()));
        Assertions.assertTrue(texto.contains(paciente.getNome()));
        Assertions.assertTrue(texto.contains(emissaoPadrao.toString()));
        Assertions.assertTrue(texto.contains(validadePadrao.toString()));
    }
    
}
