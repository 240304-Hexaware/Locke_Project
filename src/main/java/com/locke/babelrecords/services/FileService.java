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
import java.util.Arrays;
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

    public List<String> readFields(char[] data, List<SpecField> specs) throws IOException {
        List<String> fields = new ArrayList<>();
        List<Integer> starts = specs.stream().map(SpecField::getStart).toList();
        List<Integer> ends = specs.stream().map(SpecField::getEnd).toList();

        for (int fieldIndex = 0; fieldIndex < starts.size(); fieldIndex++) {
            StringBuilder fieldBuilder = new StringBuilder();

            for(int i = starts.get(fieldIndex); i <= ends.get(fieldIndex); i++) {
                fieldBuilder.append(data[i]);
            }

            fields.add(fieldBuilder.toString().trim());
        }

        return fields;
    }

    private int calculateItemSize(List<SpecField> specs) {
        // Reduce will return the last element
        return specs.stream().map(SpecField::getEnd).reduce((first, second) -> second).get() + 1;
    }

    public List<FileField> buildRecord(List<String> fields, List<SpecField> specs) {
        List<FileField> mergedFields = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            mergedFields.add(new FileField(specs.get(i).getName(), fields.get(i), specs.get(i).getDataType()));
        }

        return mergedFields;
    }

    public ParsedFile buildParsedFile(MultipartFile file, List<SpecField> specs, String userId, String fileName) throws IOException {
        char[] data = new String(file.getBytes(), StandardCharsets.UTF_8).toCharArray();
        int recordSize = calculateItemSize(specs);
        int numRecords = data.length / recordSize;

        ParsedFile parsedFile = new ParsedFile(userId, fileName);
        for (int i = 0; i < numRecords; i++) {
            List<String> extractedFields = readFields(Arrays.copyOfRange(data, (i * recordSize), recordSize + (i * recordSize)), specs);
            List<FileField> record = buildRecord(extractedFields, specs);

            parsedFile.addRecord(record);
        }

        return parsedFile;
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

    public void uploadFlatFile(String userId, String name, MultipartFile file, String specFileId) throws ItemAlreadyExistsException, IOException {
        try {
            if(parsedFileRepository.findByName(name).isEmpty()) {
                char[] data = new String(file.getBytes(), StandardCharsets.UTF_8).toCharArray();
                SpecFile specs = specFileRepository.findById(specFileId).orElseThrow();
                ParsedFile parsedFile = buildParsedFile(file, specs.getSpecs(), userId, name);
                this.parsedFileRepository.save(parsedFile);
            } else {
                throw new ItemAlreadyExistsException("There is already a flat file with that name.");
            }
        } catch (IOException e ) {
            throw new IOException("Could not parse flat file");
        }

    }

    public List<SpecFile> getUserSpecFiles(String userId) {
        return specFileRepository.findByUserId(userId);
    }

    public List<ParsedFile> getUserParsedFiles(String userId) {
        return parsedFileRepository.findByUserId(userId);
    }

    public SpecFile getSpecFileById(String id) throws ItemNotFoundException {
        return specFileRepository.findById(id).orElseThrow( () -> new ItemNotFoundException("Spec File Not Found."));
    }

    public ParsedFile getParsedFileById(String id) throws ItemNotFoundException {
        return parsedFileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Flat File Not Found."));
    }
}
