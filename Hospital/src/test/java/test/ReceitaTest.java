package test;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.servicos_hospital.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class ReceitaTest {

    private Medico medico;
    private Paciente paciente;
    private LocalDate hoje;
    private LocalDate mesQueVem;

    @BeforeEach
    public void setUp() throws DadosInvalidosException {
        // Limpeza dos registros estáticos para evitar erro de duplicidade
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();

        hoje = LocalDate.now();
        mesQueVem = hoje.plusMonths(1);

        // Médicos e Pacientes com e-mails que a Apache Validator aceita
        medico = new Medico("Dr. House", "999.999.999-99", "house@hospital.com", "senha123", "Diagnóstico");
        paciente = new Paciente("Wilson", "222.222.222-22", "wilson@oncologia.com", "senha123", new Historico(), "Endereço", "32 91111-2222");
    }

    @Test
    public void testReceitaValida() throws DadosInvalidosException {
        Receita r = new Receita("Dipirona", "500mg", paciente, medico, hoje, mesQueVem);
        
        Assertions.assertEquals("Dipirona", r.getMedicamento());
        Assertions.assertEquals(hoje, r.getDataEmissao());
        Assertions.assertEquals(mesQueVem, r.getValidade());
    }

    @Test
    public void testValidadeAnteriorEmissao() {
        try {
            // Tenta criar uma receita com validade ONTEM
            new Receita("Teste", "1", paciente, medico, hoje, hoje.minusDays(1));
            Assertions.fail("Deveria ter lançado DataInvalidaException");
        } catch (DataInvalidaException e) {
            Assertions.assertTrue(e.getMessage().contains("não pode ser anterior"));
        } catch (DadosInvalidosException e) {
            Assertions.fail("Lançou a exceção errada");
        }
    }

    @Test
    public void testMedicamentoVazio() {
        try {
            new Receita("", "1", paciente, medico, hoje, mesQueVem);
            Assertions.fail("Deveria ter falhado pois o medicamento está vazio");
        } catch (DadosInvalidosException e) {
            Assertions.assertEquals("É obrigatório o informar o nome do medicamento ", e.getMessage());
        }
    }

    @Test
    public void testQuantidadeVazia() {
        try {
            new Receita("Dipirona", "  ", paciente, medico, hoje, mesQueVem);
            Assertions.fail("Deveria ter falhado pois a quantidade está vazia");
        } catch (DadosInvalidosException e) {
            Assertions.assertEquals("A quantidade deve ser informada! ", e.getMessage());
        }
    }
}