package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class UsuarioTest {
    
    private Historico h;
    
    public UsuarioTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        h = new Historico();
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void usuarioValido(){
        Paciente p = new Paciente("Ana", "123.456.789-00", "login", "senha", h, "Rua A", "32 9 8888-0000");
        
        Assertions.assertEquals("Ana",p.getNome());
        Assertions.assertEquals("12345678900",p.getCpf());
        Assertions.assertEquals("login",p.getLogin());
        Assertions.assertEquals("Paciente",p.getTipo());
    }
    
     @Test
     public void testCpfLimpo() throws DadosInvalidosException{
         
        Paciente p = new Paciente("joao", "123.456.789-00", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
        Assertions.assertEquals( "12345678900", p.getCpf(), "O cpf deve conter apenas numeros");
     }
     
     @Test
     public void testCpfInvalido() throws DadosInvalidosException{
        
         try {
            new Paciente("joao", "123.456.7-0", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
            Assertions.fail("Deveria lancar exceção poois o cps passado no construtor é inválido");
         } catch (DadosInvalidosException e) {
             Assertions.assertTrue(e.getMessage().contains("11 dígitos"));
         }
     }
     
     @Test
     public void testSetNomeVazio() throws DadosInvalidosException{
         
        Paciente p = new Paciente("joao", "123.456.789-00", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
         
        try {
            p.setNome("  ");
            Assertions.fail("Deve ser lançado uma exceção DadosInvalidosException para nome vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().contains("não informado") || e.getMessage().contains("inválido"));
        }
     }
     
    @Test
    public void testValidarSenhaCorreta() throws DadosInvalidosException {
        
        Paciente p = new Paciente("joao", "123.456.789-00", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
        Assertions.assertTrue(p.validarSenha("senha"));
        Assertions.assertFalse(p.validarSenha("senha Errada"));
    }
 
    @Test
    public void testLoginInvalido() throws DadosInvalidosException{
        try {
            new Paciente("joao", "123.456.7-0", " ", "senha", h, "Rua A", "(32) 9 9999-0000");
            Assertions.fail("Deveria falhar por login vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("login"));
        }
    }
    
    @Test
    public void testSetLoginInvalido() throws DadosInvalidosException{
        Paciente p = new Paciente("joao", "123.456.7-0", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
        try {
            p.setLogin(" ");
            Assertions.fail("Deveria falhar por login vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("login"));
        }
    }
    
    @Test
    public void testSenhaInvalidaConstrutor() throws DadosInvalidosException{
        try {
            new Paciente("joao", "123.456.7-0", "login", " ", h, "Rua A", "(32) 9 9999-0000");
            Assertions.fail("Deveria falhar por Senha vazia");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("inválida"));
        }
    }
    
    @Test
    public void testSetSenhaInvalida() throws DadosInvalidosException{
        Paciente p = new Paciente("joao", "123.456.7-0", "login", "senha", h, "Rua A", "(32) 9 9999-0000");
        try {
            p.setSenha(" ");
            Assertions.fail("Deveria falhar por senha vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("inválida"));
        }
    }
}
