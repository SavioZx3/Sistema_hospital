package ufjf.dcc025.hospital.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Persistencia {
    
    private static final String CAMINHO_ARQUIVO = "database/hospital_dados.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static void salvar(Object dados){
        
        try {
            Files.createDirectories(Paths.get("database"));
            
            try (FileWriter writer = new FileWriter(CAMINHO_ARQUIVO)){
                gson.toJson(dados, writer);
            } 
        }catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    public static HospitalData carregar() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        
        if(!arquivo.exists())
            return HospitalData.criarComDadosDefault();
        
        try (FileReader reader = new FileReader(CAMINHO_ARQUIVO)){
            
            return gson.fromJson(reader, HospitalData.class);
        } catch (IOException e) {
            return HospitalData.criarComDadosDefault();
        }
    }
}
