package ufjf.dcc025.hospital.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import ufjf.dcc025.hospital.servicos_hospital.consulta.Consulta;
import ufjf.dcc025.hospital.usuarios.medico.Medico;
import ufjf.dcc025.hospital.usuarios.paciente.Paciente;

import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Persistencia {

    private static final String CAMINHO_ARQUIVO = "database/hospital_dados.json";

    // Gson configurado para LocalDate, LocalTime e Duration
    private static final Gson gson = new GsonBuilder()
            // LocalDate
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) -> LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
            // LocalTime
            .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) -> LocalTime.parse(json.getAsString()))
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (time, type, context) -> new JsonPrimitive(time.toString()))
            // Duration (salva em segundos)
            .registerTypeAdapter(Duration.class, (JsonSerializer<Duration>) (duration, type, context) -> new JsonPrimitive(duration.toSeconds()))
            .registerTypeAdapter(Duration.class, (JsonDeserializer<Duration>) (json, type, context) -> {
                try {
                    return Duration.ofSeconds(json.getAsLong());
                } catch (Exception e) {
                    throw new JsonParseException(e);
                }
            })
            .setPrettyPrinting()
            .create();

    /**
     * Salva os dados do hospital no arquivo JSON. Cria a pasta "database" se
     * não existir.
     */
    public static void salvar(Object dados) {
        try {
            Files.createDirectories(Paths.get("database"));

            try (FileWriter writer = new FileWriter(CAMINHO_ARQUIVO)) {
                gson.toJson(dados, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados do hospital do arquivo JSON. Se o arquivo não existir ou
     * estiver inválido, cria dados default.
     */
    public static HospitalData carregar() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            HospitalData dadosDefault = HospitalData.criarComDadosDefault();
            salvar(dadosDefault);
            return dadosDefault;
        }

        try (FileReader reader = new FileReader(CAMINHO_ARQUIVO)) {

            HospitalData dadosCarregados = gson.fromJson(reader, HospitalData.class);

            //Reconstruir consultas globais
            for (Consulta c : dadosCarregados.getConsultas()) {
                c.reconstruirReferencias(dadosCarregados.getMedicos(), dadosCarregados.getPacientes());
            }

            //Reconstruir consultas da agenda do médico
            for (Medico m : dadosCarregados.getMedicos()) {
                if (m.getAgenda() != null) {
                    m.getAgenda().setMedico(m);
                    for (Consulta c : m.getAgenda().getConsultas()) {
                        c.reconstruirReferencias(dadosCarregados.getMedicos(), dadosCarregados.getPacientes());
                    }
                }
            }

            //RECONSTRUIR CONSULTAS DO HISTÓRICO DO PACIENTE
            for (Paciente p : dadosCarregados.getPacientes()) {
                if (p.getHistorico() != null) {
                    for (Consulta c : p.getHistorico().getConsultas()) {
                        c.reconstruirReferencias(dadosCarregados.getMedicos(), dadosCarregados.getPacientes());
                    }
                }
            }

            if (dadosCarregados == null) {
                HospitalData dadosDefault = HospitalData.criarComDadosDefault();
                salvar(dadosDefault);
                return dadosDefault;
            }
            return dadosCarregados;
        } catch (IOException | com.google.gson.JsonParseException e) {
            // Em caso de erro de leitura ou JSON inválido, retorna dados default
            HospitalData dadosDefault = HospitalData.criarComDadosDefault();
            salvar(dadosDefault);
            return dadosDefault;
        }
    }
}
