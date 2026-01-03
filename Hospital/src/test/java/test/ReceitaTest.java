package test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ufjf.dcc025.hospital.servicos_hospital.Receita;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Historico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;


public class ReceitaTest {
    
    private Medico medico;
    private Paciente paciente;
    private Historico h;
    
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
    }
    
    @AfterEach
    public void tearDown() {
    }

    
     @Test
     public void testReceitaValida(){
       
        String medicamento = "Dipirona";
        String quantidade = "500mg de 6 em 6 horas";
        
        LocalDate dataEmissao = LocalDate.of(2025, 12, 30);
        LocalDate dataValidade = LocalDate.of(2026, 1, 15);
         
            Receita receita = new Receita(medicamento, quantidade, paciente, medico, dataEmissao, dataValidade);
            
            Assertions.assertNotNull(receita);
            
            Assertions.assertEquals("Dipirona", receita.getMedicamento());
            Assertions.assertEquals(dataEmissao, receita.getDataEmissao());
            Assertions.assertEquals(dataValidade, receita.getValidade()); 
     }
     
    @Test
    public void testReceitaComDataValidadeInvalida() {
    
        String medicamento = "Dipirona";
        String quantidade = "500mg de 6 em 6 horas";
        
        LocalDate dataEmissao = LocalDate.of(2025, 1, 2);
        LocalDate dataValidade = LocalDate.of(2025, 1, 1);
     
    }
    
}
