package com.shopme.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void save(String uploadDir, String fileName, MultipartFile multipartFile);
    void remove(String uploadDir, String fileName);
}
