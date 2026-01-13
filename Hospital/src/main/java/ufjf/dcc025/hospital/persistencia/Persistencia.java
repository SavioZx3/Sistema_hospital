package ufjf.dcc025.hospital.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

public class Persistencia {

    private static final String CAMINHO_ARQUIVO = "database/hospital_dados.json";

    //Isso garante que a data da Consulta e horarioInicio / horarioFim sejam salvos corretamente
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) -> LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) -> new JsonPrimitive(date.toString()))
            .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) -> LocalTime.parse(json.getAsString()))
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (time, type, context) -> new JsonPrimitive(time.toString()))
            .setPrettyPrinting()
            .create();

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

    public static HospitalData carregar() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return HospitalData.criarComDadosDefault();
        }

        try (FileReader reader = new FileReader(CAMINHO_ARQUIVO)) {

            return gson.fromJson(reader, HospitalData.class);
        } catch (IOException e) {
            return HospitalData.criarComDadosDefault();
        }
    }
}
