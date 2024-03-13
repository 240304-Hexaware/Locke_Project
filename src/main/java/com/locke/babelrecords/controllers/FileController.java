package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.services.FileService;
import com.locke.babelrecords.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("api/v1/files")
public class FileController {
    private FileService fileService;

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
    public void postFlatFile(@PathVariable("id") String userId, @RequestPart("file")MultipartFile flatFile, @RequestParam String flatFileName, @RequestParam String specFileId) throws ItemNotFoundException, IOException, ItemAlreadyExistsException {
        fileService.uploadFlatFile(userId, flatFileName, flatFile, specFileId);
    }
}
