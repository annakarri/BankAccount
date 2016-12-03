package com.app.controller;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.app.AppException;
import com.app.Constants;
import com.app.service.StorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.app.Constants.ERROR_FILE_NOT_UPLOADED;
import static com.app.Constants.ERROR_SAVING_FILE;
import static com.app.Constants.SUCCESS_FILE_UPLOADED;

@Controller
public class BankController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> ajaxfileUpload(@RequestParam("uploadfile") MultipartFile file) {
        String message;

        if (StringUtils.isBlank(file.getOriginalFilename())) {
            message = ERROR_FILE_NOT_UPLOADED;
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            try {
                storageService.clearStorage();
                Long id = storageService.store(file);
                message = String.format(SUCCESS_FILE_UPLOADED, file.getOriginalFilename());
                return new ResponseEntity<>(message, HttpStatus.OK);
            } catch (IOException e) {
                message = ERROR_SAVING_FILE;
                e.printStackTrace();
            } catch (AppException e) {
                message = e.getMessage();
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }
}
