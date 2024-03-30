package com.locke.babelrecords.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.*;
import com.locke.babelrecords.models.Record;
import com.locke.babelrecords.repositories.ParsedFileRepository;
import com.locke.babelrecords.repositories.RecordRepository;
import com.locke.babelrecords.repositories.SpecFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@Service
public class FileService {
  private final SpecFileRepository specFileRepository;
  private final ParsedFileRepository parsedFileRepository;

  private final RecordRepository recordRepository;

  @Autowired
  public FileService(SpecFileRepository specFileRepository, ParsedFileRepository parsedFileRepository, RecordRepository recordRepository) {
    this.specFileRepository = specFileRepository;
    this.parsedFileRepository = parsedFileRepository;
    this.recordRepository = recordRepository;
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

  @SuppressWarnings("UnstableApiUsage")
  public List<String> readFields(char[] data, List<SpecField> specs) throws IOException {
    return Streams.zip(
        specs.stream().map(SpecField::getStart), specs.stream().map(SpecField::getEnd),
        (start, end) -> new String(Arrays.copyOfRange(data, start, end)).trim()
    ).toList();
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private int calculateItemSize(List<SpecField> specs) {
    // Reduce will return the last element
    return specs.stream().map(SpecField::getEnd).reduce((first, second) -> second).get() + 1;
  }

  public ParsedFile buildParsedFile(MultipartFile file, List<SpecField> specs, String userId, String specId, String fileName) throws IOException {
    char[] data = new String(file.getBytes(), StandardCharsets.UTF_8).toCharArray();
    int recordSize = calculateItemSize(specs);
    int numRecords = data.length / recordSize;
    var parsedRecords = new ArrayList<Record>(numRecords);

    var parsedFile = new ParsedFile(fileName);
    for ( int i = 0; i < numRecords; i++ ) {
      var record = new Record();
      var fieldValues = readFields(Arrays.copyOfRange(data, (i * recordSize), recordSize + (i * recordSize)), specs);
      SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

      Streams.forEachPair(fieldValues.stream(), specs.stream(), (value, spec) -> {
        record.addField(spec.getName(), switch ( spec.getDataType() ) {
          case "Integer" -> Integer.valueOf(value);
          case "Date" -> {
            try {
              yield sDateFormat.parse(value.replace("/", "-"));
            } catch ( ParseException e ) {
              throw new RuntimeException(e);
            }
          }
          case "Time" -> Duration.ofSeconds(Integer.parseInt(value)).toMinutes();
          default -> value;
        });
      });

      parsedRecords.add(record);
    }
    List<String> recordIds = parsedRecords.stream().map(record -> this.recordRepository.save(record).getId()).toList();
    parsedFile.setRecordIds(recordIds);
    return parsedFile;
  }

  public SpecFile uploadSpecFile(String userId, String name, MultipartFile specFile) throws ItemAlreadyExistsException, IOException {
    // Spec Files must be owned by a user and have a unique within said user's spec files
    var parsedFields = parseSpecFile(specFile);
    var userSpecs = specFileRepository.findByUserId(userId);
    if ( !userSpecs.stream().map(SpecFile::getName).toList().contains(name) ) {
      return this.specFileRepository.save(new SpecFile(userId, name, parsedFields));
    } else {
      throw new ItemAlreadyExistsException("There is already a spec file with that name.");
    }
  }

  public ParsedFile uploadFlatFile(String userId, String name, MultipartFile file, String specFileId) throws ItemAlreadyExistsException, IOException, ParseException {
    try {
      Path filepath = Paths.get(System.getProperty("user.dir"), "flatfiles", userId);
      // Creates user dir if not exists, else NOP
      Files.createDirectories(filepath);
      file.transferTo(Paths.get(filepath.toString(), name));
    } catch ( IOException e ) {
      throw new IOException("Could not write file to disk");
    }

    Optional<ParsedFile> sameNameParsed = parsedFileRepository.findByName(name);
    try {
      if ( sameNameParsed.isEmpty() || !sameNameParsed.get().getId().equals(userId) ) {
        var specFile = specFileRepository.findById(specFileId).orElseThrow();
        var parsedFile = buildParsedFile(file, specFile.getFields(), userId, specFile.getId(), name);

        return this.parsedFileRepository.save(parsedFile);
      } else {
        throw new ItemAlreadyExistsException("There is already a flat file with that name.");
      }
    } catch ( IOException e ) {
      throw new IOException("Could not parse flat file");
    }

  }

  public List<SpecFile> findAllSpecFiles() {
    return specFileRepository.findAll();
  }

  public List<SpecFile> getUserSpecFiles(String userId) {
    return specFileRepository.findByUserId(userId);
  }

  public List<ParsedFile> getUserParsedFiles(List<String> userId) {
    return parsedFileRepository.findAllById(userId);
  }

  public List<String> getUserFilePaths(String userId) throws IOException {
    var files = new ArrayList<String>();
    try ( Stream<Path> paths = Files.walk(Paths.get(System.getProperty("user.dir"), "flatfiles", userId)) ) {
      paths
          .filter(Files::isRegularFile)
          .forEach(path -> files.add(path.toString()));
    }
    return files;
  }

  public ByteArrayResource getUserFile(String userId, String filename) throws IOException {
    Path path = Paths.get(System.getProperty("user.dir"), "flatfiles", userId, filename);
    return new ByteArrayResource(Files.readAllBytes(path));
  }

  public SpecFile getSpecFileById(String id) throws ItemNotFoundException {
    return specFileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Spec File Not Found."));
  }

  public ParsedFile getParsedFileById(String id) throws ItemNotFoundException {
    return parsedFileRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Flat File Not Found."));
  }

  public void addParsedToSpec(String specId, ParsedFile parsedFile) {
    var specFile = specFileRepository.findById(specId).orElseThrow();
    specFile.addParsedFileId(parsedFile.getId());
    specFile.addRecordIds(parsedFile.getRecordIds());
    specFileRepository.save(specFile);
  }
}
