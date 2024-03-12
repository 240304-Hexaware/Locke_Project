package com.locke.babelrecords.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.repositories.SpecFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public void uploadSpecFile(String userId, String name, List<SpecField> specs) {
        System.out.println(userId);
        this.specFileRepository.save(new SpecFile(userId, name, specs));
    }

    public List<SpecFile> getUserSpecFiles(String userId) {
        return specFileRepository.findByUserId(userId);
    }
}
