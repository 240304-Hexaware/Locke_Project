package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.SpecFile;
import com.locke.babelrecords.services.FileService;
import com.locke.babelrecords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
