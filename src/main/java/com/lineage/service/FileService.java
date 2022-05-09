package com.lineage.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService {

    public void writeToFile(String json, String fileName) {
        try {
            Files.writeString(Paths.get(fileName), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
