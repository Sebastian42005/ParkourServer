package com.example.spotmap.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler<T> {

    private static final FileHandler instance = new FileHandler();
    private static final String PATH = "files/time-tables/";


    public static FileHandler getInstance() {
        return instance;
    }

    private FileHandler() { }

    public T toObject(String fileName, Class<T> clazz) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(PATH + fileName + ".txt"), clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public void writeFile(T object, String toFile) throws IOException {
        File file = new File(PATH + toFile + ".txt");

        if(!file.exists()) {
            file.createNewFile();
            writeFile(object, toFile);
            return;
        }

        FileWriter fileWriter = new FileWriter(file);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(object);

        fileWriter.write(content);

        fileWriter.close();
    }

}
