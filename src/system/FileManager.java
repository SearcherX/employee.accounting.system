package system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import system.structure.Organization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class FileManager {
    //метод загрузки информации об организации из JSON-файла
    public static Organization loadJSON(String fileName) {
        Organization org = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            org = mapper.readValue(Paths.get(fileName).toFile(), Organization.class);
        } catch (IOException ex) {
            System.out.println("Не удалось загрузить данные организации");
            System.out.println(ex.getMessage());
        }
        return org;
    }

    //метод сохранения информации об организации в JSON-файл
    public static void saveJSON(String fileName, Organization org) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
            mapper.writeValue(Paths.get(fileName).toFile(), org);

        } catch (Exception ex) {
            System.out.println("Не удалось сохранить данные в файл");
            System.out.println(ex.getMessage());
        }
    }

    //метод записи отчета в файл
    public static void writeReportToFile(String fileName, String report) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(report);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
