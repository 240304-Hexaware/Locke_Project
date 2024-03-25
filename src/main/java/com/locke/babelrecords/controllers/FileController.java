package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController()
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/files")
public class FileController {
  private FileService fileService;

  @GetMapping("spec-files/user/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<SpecFile> getUserSpecFiles(@PathVariable("id") String userId) {
    return fileService.getUserSpecFiles(userId);
  }

  @GetMapping("flat-files/user/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<ParsedFile> getUserParsedFiles(@PathVariable("id") String userId) {
    return fileService.getUserParsedFiles(userId);
  }

  @PostMapping(value = "/spec-file/user/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> postSpecFile(@PathVariable("id") String userId, @RequestPart("file") MultipartFile specFile, @RequestParam String specName) throws IOException, ItemAlreadyExistsException {
    List<SpecField> parsedFile = fileService.parseSpecFile(specFile);
    fileService.uploadSpecFile(userId, specName, parsedFile);
    return new ResponseEntity<>(parsedFile, HttpStatus.CREATED);
  }

  @Autowired
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/spec-files")
  @ResponseStatus(HttpStatus.OK)
  public List<SpecFile> getById() throws ItemNotFoundException {
    return fileService.findAllSpecFiles();
  }

  @PostMapping("/flat-file/user/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  public void postFlatFile(@PathVariable("id") String userId, @RequestPart("file") MultipartFile flatFile, @RequestParam String flatFileName, @RequestParam String specFileId) throws ItemNotFoundException, IOException, ItemAlreadyExistsException {
    fileService.uploadFlatFile(userId, flatFileName, flatFile, specFileId);
  }

  @ExceptionHandler(ItemAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String userAlreadyExists(ItemAlreadyExistsException e) {
    return e.getMessage();
  }
}
