package com.lineage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lineage.domain.Profile;

import java.io.File;
import java.util.List;

public class ProfileService {
    private static final String FILE_NAME = "profiles.json";

    private final FileService fileService;
    private final ObjectMapper objectMapper;

    public ProfileService() {
        this.fileService = new FileService();
        this.objectMapper = new ObjectMapper();

        try {
            File file = new File(FILE_NAME);
            if(!file.createNewFile()) {
                System.out.println("File already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveProfile(Profile profile){
        List<Profile> profileList = getProfiles();

        profileList.stream()
                .filter(pr -> pr.getName().equalsIgnoreCase(profile.getName()))
                .findFirst()
                .ifPresent(profileList::remove);

        profileList.add(profile);

        try {
            fileService.writeToFile(objectMapper.writeValueAsString(profileList), FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProfile(Profile profile){
        List<Profile> profileList = getProfiles();
        profileList.stream()
                .filter(pr -> pr.getName().equalsIgnoreCase(profile.getName()))
                .findFirst()
                .ifPresent(profileList::remove);
        try {
            fileService.writeToFile(objectMapper.writeValueAsString(profileList), FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Profile> getProfiles() {
        String json = fileService.readFromFile(FILE_NAME);
        try {
            return objectMapper.readValue(json, new TypeReference<List<Profile>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
