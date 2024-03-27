package com.locke.babelrecords.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.*;
import com.locke.babelrecords.repositories.MetaTagRepository;
import com.locke.babelrecords.repositories.ParsedFileRepository;
import com.locke.babelrecords.repositories.SpecFileRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileService {
  private SpecFileRepository specFileRepository;
  private ParsedFileRepository parsedFileRepository;
  private MetaTagRepository metaTagRepository;

  @Autowired
  public FileService(SpecFileRepository specFileRepository, ParsedFileRepository parsedFileRepository, MetaTagRepository metaTagRepository) {
    this.specFileRepository = specFileRepository;
    this.parsedFileRepository = parsedFileRepository;
    this.metaTagRepository = metaTagRepository;
  }

  public List<SpecField> parseSpecFile(MultipartFile specFile) throws IOException {
    LinkedHashMap<String, SpecField> map = new ObjectMapper()
        .readValue(specFile.getBytes(), new TypeReference<LinkedHashMap<String, SpecField>>() {
        });

    for ( String s : map.keySet() ) {
      map.get(s).setName(s);
    }

    return new ArrayList<SpecField>(map.values());
  }

  public List<String> readFields(char[] data, List<SpecField> specs) throws IOException {
    return Streams.zip(
        specs.stream().map(SpecField::getStart), specs.stream().map(SpecField::getEnd),
        (start, end) -> new String(Arrays.copyOfRange(data, start, end)).trim()
    ).toList();
  }

  private int calculateItemSize(List<SpecField> specs) {
    // Reduce will return the last element
    return specs.stream().map(SpecField::getEnd).reduce((first, second) -> second).get() + 1;
  }

  public List<FileField> buildRecord(List<String> fields, List<SpecField> specs) {
    return Streams.zip(
        fields.stream(), specs.stream(),
        (field, spec) -> new FileField(spec.getName(), field, spec.getDataType())
    ).toList();
  }

  public ParsedFile buildParsedFile(MultipartFile file, List<SpecField> specs, String userId, String fileName) throws IOException {
    char[] data = new String(file.getBytes(), StandardCharsets.UTF_8).toCharArray();
    int recordSize = calculateItemSize(specs);
    int numRecords = data.length / recordSize;

    ParsedFile parsedFile = new ParsedFile(userId, fileName);
    for ( int i = 0; i < numRecords; i++ ) {
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
    List<SpecFile> userSpecs = specFileRepository.findByUserId(userId);
    if ( !userSpecs.stream().map(SpecFile::getName).toList().contains(name) ) {
      this.specFileRepository.save(new SpecFile(userId, name, specs));
    } else {
      throw new ItemAlreadyExistsException("There is already a spec file with that name.");
    }
  }

  public void uploadMetaData(MultipartFile file, SpecFile specs, String userId, String filename, String specFileId) throws IOException {
    char[] data = new String(file.getBytes(), StandardCharsets.UTF_8).toCharArray();
    int recordSize = calculateItemSize(specs.getSpecs());
    int numRecords = data.length / recordSize;
    this.metaTagRepository.save(new MetaTag(userId, filename, specFileId, numRecords));
  }

  public void uploadFlatFile(String userId, String name, MultipartFile file, String specFileId) throws ItemAlreadyExistsException, IOException {
    try {
      Path filepath = Paths.get(System.getProperty("user.dir"), "flatfiles", (userId + "_" + name));
      file.transferTo(filepath);
    } catch ( IOException e ) {
      throw new IOException("Could not write file to disk");
    }

    Optional<ParsedFile> sameNameParsed = parsedFileRepository.findByName(name);
    try {
      if ( sameNameParsed.isEmpty() || !sameNameParsed.get().getId().equals(userId) ) {
        SpecFile specs = specFileRepository.findById(specFileId).orElseThrow();
        ParsedFile parsedFile = buildParsedFile(file, specs.getSpecs(), userId, name);

        uploadMetaData(file, specs, userId, name, specFileId);
        this.parsedFileRepository.save(parsedFile);
      } else {
        throw new ItemAlreadyExistsException("There is already a flat file with that name.");
      }
    } catch ( IOException e ) {
      throw new IOException("Could not parse flat file");
    }

  }

  public List<SpecFile> getUserSpecFiles(String userId) {
    return specFileRepository.findByUserId(userId);
  }

  public List<ParsedFile> getUserParsedFiles(String userId) {
    return parsedFileRepository.findByUserId(userId);
  }

  public List<String> getUserFilePaths(String userId) throws IOException {
    ArrayList<String> files = new ArrayList<>();
    try ( Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir"), "flatfiles")) ) {
      paths
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().contains(userId))
          .forEach(path -> files.add(path.toString().split("_")[1]));
    }
    return files;
  }

  public ByteArrayResource getUserFile(String userId, String filename) throws IOException {
    Path path = Paths.get(System.getProperty("user.dir"), "flatfiles", (userId + "_" + filename));
    return new ByteArrayResource(Files.readAllBytes(path));
  }

  public SpecFile getSpecFileById(String id) throws ItemNotFoundException {
    return specFileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Spec File Not Found."));
  }

  public ParsedFile getParsedFileById(String id) throws ItemNotFoundException {
    return parsedFileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Flat File Not Found."));
  }
}
