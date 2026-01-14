package test;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.exception.DadosInvalidosException;
import ufjf.dcc025.hospital.exception.DataInvalidaException;
import ufjf.dcc025.hospital.outros.Cpf;
import ufjf.dcc025.hospital.outros.Email;
import ufjf.dcc025.hospital.servicos_hospital.documento_medico.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Especialidade;
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
        return new Medico("Dr. House", "999.999.999-99", "house@hospital.com", "senha123", Especialidade.CARDIOLOGIA);
    }

    private Paciente criarPacientePadrao() throws DadosInvalidosException {
        return new Paciente("James Wilson", "222.222.222-22", "wilson@hospital.com", "senha123", "32 91111-2222", "Princeton", "Mercer", "Baker Street", "221B"
        );
    }

}