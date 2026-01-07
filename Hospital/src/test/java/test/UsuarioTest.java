package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class UsuarioTest {
    
    private Historico h;
    
    @BeforeEach
    public void setUp() {
        h = new Historico();
        // Como Cpf e Email guardam listas estáticas para evitar duplicados,
        // precisamos limpar essas listas antes de cada teste para não dar erro de "já cadastrado".
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();
    }

    @Test
    public void usuarioValido() throws DadosInvalidosException {
        Paciente p = new Paciente("Ana", "123.456.789-00", "ana@email.com", "senha123", h, "Rua A", "32 9 8888-0000");
        
        Assertions.assertEquals("Ana", p.getNome());
        Assertions.assertEquals("12345678900", p.getCpf());
        Assertions.assertEquals("ana@email.com", p.getEmail());
        Assertions.assertEquals("PACIENTE", p.getTipo());
    }
    
    @Test
    public void testCpfLimpo() throws DadosInvalidosException {
        Paciente p = new Paciente("joao", "123.456.789-00", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
        Assertions.assertEquals("12345678900", p.getCpf(), "O cpf deve conter apenas numeros");
    }
     
    @Test
    public void testCpfInvalido() {
        try {
            // CPF com formato errado (faltando dígitos)
            new Paciente("joao", "123.456", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
            Assertions.fail("Deveria lancar exceção pois o cpf é inválido");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().contains("11 dígitos"));
        }
    }
     
    @Test
    public void testSetNomeVazio() throws DadosInvalidosException {
        Paciente p = new Paciente("joao", "123.456.789-00", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
        try {
            p.setNome("  ");
            Assertions.fail("Deve ser lançado uma exceção para nome vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("não informado") || e.getMessage().toLowerCase().contains("inválido"));
        }
    }
     
    @Test
    public void testValidarSenhaCorreta() throws DadosInvalidosException {
        Paciente p = new Paciente("joao", "123.456.789-00", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
        Assertions.assertTrue(p.validarSenha("senha123"));
        Assertions.assertFalse(p.validarSenha("senha Errada"));
    }

    @Test
    public void testEmailInvalido() {
        try {
            // Tenta criar com email sem formato válido
            new Paciente("joao", "123.456.789-00", "email_sem_arroba", "senha123", h, "Rua A", "(32) 9 9999-0000");
            Assertions.fail("Deveria falhar por formato de email inválido");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("e-mail") || e.getMessage().toLowerCase().contains("formato"));
        }
    }
    
    @Test
    public void testSetEmailVazio() throws DadosInvalidosException {
        Paciente p = new Paciente("joao", "123.456.789-00", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
        try {
            p.setEmail(" ");
            Assertions.fail("Deveria falhar por Email vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("e-mail"));
        }
    }
    
    @Test
    public void testSetSenhaInvalida() throws DadosInvalidosException {
        Paciente p = new Paciente("joao", "123.456.789-00", "joao@email.com", "senha123", h, "Rua A", "(32) 9 9999-0000");
        try {
            p.setSenha(" ");
            Assertions.fail("Deveria falhar por senha vazia");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("inválida") || e.getMessage().toLowerCase().contains("vazia"));
        }
    }
}