package com.app.service;

/**
 * Created by anna.karri on 01/12/16.
 */

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.app.AppException;

public interface StorageService {

	void clearStorage();

	Long store(MultipartFile file) throws IOException, AppException;
}
