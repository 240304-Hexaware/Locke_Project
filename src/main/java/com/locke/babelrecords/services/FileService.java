package com.locke.babelrecords.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.FileField;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.repositories.ParsedFileRepository;
import com.locke.babelrecords.repositories.SpecFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FileService {
    private SpecFileRepository specFileRepository;
    private ParsedFileRepository parsedFileRepository;

    @Autowired
    public FileService(SpecFileRepository specFileRepository, ParsedFileRepository parsedFileRepository) {
        this.specFileRepository = specFileRepository;
        this.parsedFileRepository = parsedFileRepository;
    }
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
        List<Integer> starts = specs.stream().map(SpecField::getStart).toList();
        List<Integer> ends = specs.stream().map(SpecField::getEnd).toList();

        for (int fieldIndex = 0; fieldIndex < starts.size(); fieldIndex++) {
            StringBuilder fieldBuilder = new StringBuilder();
            char[] data = new String(dataFile.getBytes(), StandardCharsets.UTF_8).toCharArray();

            for(int i = starts.get(fieldIndex); i <= ends.get(fieldIndex); i++) {
                fieldBuilder.append(data[i]);
            }

            fields.add(fieldBuilder.toString().trim());
        }

        return fields;
    }

    public ParsedFile buildFile(String userId, String name, List<String> fields, List<SpecField> specs) {
        List<FileField> mergedFields = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            mergedFields.add(new FileField(specs.get(i).getName(), fields.get(i), specs.get(i).getDataType()));
        }

        return new ParsedFile(userId, name, mergedFields);
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
    public void uploadFlatFile(String userId, String name, MultipartFile file, String specFileId) throws ItemNotFoundException, ItemAlreadyExistsException, IOException {
        if(parsedFileRepository.findByName(name).isEmpty()) {
            SpecFile specs = specFileRepository.findById(specFileId).orElseThrow();
            List<String> extractedFields = readFields(file, specs.getSpecs());
            ParsedFile parsedFile = buildFile(userId, name, extractedFields, specs.getSpecs());
            this.parsedFileRepository.save(parsedFile);
        } else {
            throw new ItemAlreadyExistsException("There is already a flat file with that name.");
        }
    }

    public void uploadParsedFlatFile() {

    }

    public List<SpecFile> getUserSpecFiles(String userId) {
        return specFileRepository.findByUserId(userId);
    }

    public SpecFile getSpecFileById(String id) throws ItemNotFoundException {
        return specFileRepository.findById(id).orElseThrow( () -> new ItemNotFoundException("Spec File Not Found."));
    }
}
