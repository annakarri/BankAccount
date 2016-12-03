package com.app.controller;

import com.app.AppException;
import com.app.Constants;
import com.app.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.app.Constants.ERROR_FILE_NOT_UPLOADED;
import static com.app.Constants.ERROR_SAVING_FILE;
import static com.app.Constants.SUCCESS_FILE_UPLOADED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class BankControllerTest {
    @InjectMocks
    BankController bankController;

    @Mock
    StorageService storageService;

    @Mock
    MultipartFile multipartFile;

    private String originalFileName = "fileName";

    @Before
    public void setup() throws IOException, AppException {
        MockitoAnnotations.initMocks(this);

        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        doNothing().when(storageService).clearStorage();
        when(storageService.store(any(MultipartFile.class))).thenReturn(1L);
    }

    @Test
    public void test_file_not_provided() {
        when(multipartFile.getOriginalFilename()).thenReturn(null);

        ResponseEntity<String> responseEntity = bankController.ajaxfileUpload(multipartFile);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(ERROR_FILE_NOT_UPLOADED);
    }

    @Test
    public void test_file_uploaded() {
        ResponseEntity<String> responseEntity = bankController.ajaxfileUpload(multipartFile);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(
                String.format(SUCCESS_FILE_UPLOADED, multipartFile.getOriginalFilename()));
    }

    @Test
    public void test_error_saving_file() throws IOException, AppException {
        when(storageService.store(any(MultipartFile.class))).thenThrow(new IOException());
        ResponseEntity<String> responseEntity = bankController.ajaxfileUpload(multipartFile);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(ERROR_SAVING_FILE);
    }

    @Test
    public void test_invalid_data_in_file() throws IOException, AppException {
        when(storageService.store(any(MultipartFile.class))).thenThrow(new AppException("app"));
        ResponseEntity<String> responseEntity = bankController.ajaxfileUpload(multipartFile);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("app");
    }
}
