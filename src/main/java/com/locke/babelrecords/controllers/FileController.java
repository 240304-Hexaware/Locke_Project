package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@RestController()
@CrossOrigin(
    origins = "http://localhost:4200",
    allowCredentials = "true",
    exposedHeaders = "Content-Disposition"
)
@RequestMapping("api/v1/files")
public class FileController {
  private FileService fileService;

  @Autowired
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("spec-files/user/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<SpecFile> getUserSpecFiles(@PathVariable("id") String userId) {
    return fileService.getUserSpecFiles(userId);
  }

  @GetMapping("parsed-files/user/{id}")
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

  @GetMapping("flat-files/paths/user/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<String> getFilePaths(@PathVariable("id") String userId) throws IOException {
    return fileService.getUserFilePaths(userId);
  }

  @GetMapping(value = "flat-files/file/{filename}/user/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> getFile(@PathVariable("id") String userId, @PathVariable String filename, HttpServletResponse response) throws IOException {
    ByteArrayResource data = fileService.getUserFile(userId, filename);

    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Disposition");
    response.addHeader("Content-Disposition", ContentDisposition.attachment().filename(filename).build().toString());
    response.setContentLength((int) data.contentLength());
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(data);
  }

  @ExceptionHandler(ItemAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String userAlreadyExists(ItemAlreadyExistsException e) {
    return e.getMessage();
  }
}
