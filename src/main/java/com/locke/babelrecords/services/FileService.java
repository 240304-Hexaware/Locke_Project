package com.locke.babelrecords.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.repositories.SpecFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FileService {
    private SpecFileRepository specFileRepository;

    @Autowired
    public FileService(SpecFileRepository specFileRepository) { this.specFileRepository = specFileRepository; }
    public List<SpecField> parseSpecFile(MultipartFile specFile) throws IOException {
        LinkedHashMap<String, SpecField> map = new ObjectMapper()
                .readValue(specFile.getBytes(), new TypeReference<LinkedHashMap<String, SpecField>>() {});

        for (String s : map.keySet()) {
            map.get(s).setName(s);
        }

        return new ArrayList<SpecField>(map.values());
    }

    public List<String> readFields(MultipartFile dataFile, List<SpecField> specs) throws IOException {
        List<String> fields = new ArrayList<>();
        List<Integer> spaces = specs.stream().map(SpecField::getStart).toList();

        for (int length : spaces) {
            StringBuilder fieldBuilder = new StringBuilder();
            char[] data = dataFile.toString().toCharArray();

            for(int i = 0; i < length; i++) {
                fieldBuilder.append(data[i]);
            }

            fields.add(fieldBuilder.toString());
        }

        return fields;
    }

    public List<SpecFile> findAllSpecFiles() {
        return specFileRepository.findAll();
    }
    public void uploadSpecFile(String userId, String name, List<SpecField> specs) throws ItemAlreadyExistsException {
        if(specFileRepository.findByName(name).isEmpty()) {
            this.specFileRepository.save(new SpecFile(userId, name, specs));
        } else {
            throw new ItemAlreadyExistsException("There is already a spec file with that name.");
        }
    }

    /*
    TODO: Everything
     */
    public void uploadFlatFile(File file) {

    }

    public void uploadParsedFlatFile() {

    }

    public List<SpecFile> getUserSpecFiles(String userId) {
        return specFileRepository.findByUserId(userId);
    }
}
