package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class UsuarioTest {
    
    private final String nomePadrao = "Ana";
    private final String cpfPadrao = "123.456.789-00";
    private final String emailPadrao = "ana@email.com";
    private final String senhaPadrao = "senha123";
    private final String telPadrao = "32 98888-0000";
    
    private final String cidadePadrao = "Juiz de Fora";
    private final String bairroPadrao = "Centro";
    private final String ruaPadrao = "Rua A";
    private final String numPadrao = "100";

    @BeforeEach
    public void setUp() {
        // Limpeza essencial para evitar erros de duplicidade nos testes estáticos
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();
    }

    /**
     * Método auxiliar seguindo a assinatura do Paciente:
     * (nome, cpf, email, senha, telefone, cidade, bairro, rua, numero)
     */
    private Paciente criarPacientePadrao() throws DadosInvalidosException {
        return new Paciente(nomePadrao, cpfPadrao, emailPadrao, senhaPadrao, 
                           telPadrao, cidadePadrao, bairroPadrao, ruaPadrao, numPadrao);
    }

    @Test
    public void usuarioValido() throws DadosInvalidosException {
        Paciente p = criarPacientePadrao();
        
        Assertions.assertEquals(nomePadrao, p.getNome());
        Assertions.assertEquals("12345678900", p.getCpf());
        Assertions.assertEquals(emailPadrao, p.getEmail());
        Assertions.assertEquals("PACIENTE", p.getTipo());
        
        // Verifica se o endereço contém a cidade esperada
        Assertions.assertTrue(p.getEndereco().getCidade().contains(cidadePadrao));
    }
   
    @Test
    public void testCpfLimpo() throws DadosInvalidosException {
        Paciente p = criarPacientePadrao();
        Assertions.assertEquals("12345678900", p.getCpf(), "O CPF deve conter apenas números");
    }
     
    @Test
    public void testCpfInvalido() {
        try {
            // CPF incompleto
            new Paciente(nomePadrao, "123.456", emailPadrao, senhaPadrao, 
                        telPadrao, cidadePadrao, bairroPadrao, ruaPadrao, numPadrao);
            Assertions.fail("Deveria lançar exceção pois o CPF é inválido");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().contains("11 dígitos"));
        }
    }
     
    @Test
    public void testSetNomeVazio() throws DadosInvalidosException {
        Paciente p = criarPacientePadrao();
        try {
            p.setNome("  ");
            Assertions.fail("Deve ser lançado uma exceção para nome vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("não informado") 
                    || e.getMessage().toLowerCase().contains("inválido"));
        }
    }
     
    @Test
    public void testValidarSenhaCorreta() throws DadosInvalidosException {
        Paciente p = criarPacientePadrao();
        Assertions.assertTrue(p.validarSenha(senhaPadrao));
        Assertions.assertFalse(p.validarSenha("senha Errada"));
    }

    @Test
    public void testEmailInvalido() {
        try {
            // Email sem @ e domínio
            new Paciente(nomePadrao, cpfPadrao, "emailInvalido", senhaPadrao, 
                        telPadrao, cidadePadrao, bairroPadrao, ruaPadrao, numPadrao);
            Assertions.fail("Deveria falhar por formato de e-mail inválido");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("e-mail") 
                    || e.getMessage().toLowerCase().contains("formato"));
        }
    }
    
    @Test
    public void testSetEmailVazio() throws DadosInvalidosException {
        Paciente p = criarPacientePadrao();
        try {
            p.setEmail(" ");
            Assertions.fail("Deveria falhar por e-mail vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertTrue(e.getMessage().toLowerCase().contains("e-mail"));
        }
    }
}