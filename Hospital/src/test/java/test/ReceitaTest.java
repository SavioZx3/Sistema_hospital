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
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

public class ReceitaTest {

    private Medico medico;
    private Paciente paciente;
    private LocalDate hoje;
    private LocalDate mesQueVem;

    @BeforeEach
    public void setUp() throws DadosInvalidosException {
        // Limpeza dos registros estáticos
        Cpf.limparCpfCadastrados();
        Email.limparEmailsCadastrados();

        this.hoje = LocalDate.now();
        this.mesQueVem = hoje.plusMonths(1);

        this.medico = criarMedicoPadrao();
        this.paciente = criarPacientePadrao();
    }

    private Medico criarMedicoPadrao() throws DadosInvalidosException {
        return new Medico("Dr. House", "999.999.999-99", "house@hospital.com", "senha123", "Diagnóstico");
    }

    private Paciente criarPacientePadrao() throws DadosInvalidosException {
        return new Paciente("James Wilson", "222.222.222-22", "wilson@hospital.com", "senha123", "32 91111-2222", "Princeton", "Mercer", "Baker Street", "221B"
        );
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
            Assertions.fail("Lançou a exceção errada (DadosInvalidos em vez de DataInvalida)");
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