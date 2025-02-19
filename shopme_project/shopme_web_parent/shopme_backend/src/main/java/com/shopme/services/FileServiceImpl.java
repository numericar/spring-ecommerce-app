package com.shopme.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path; // ใช้ในการเก็บ path ของไฟล์
import java.nio.file.Paths; // ใช้ในการจัดการ path ของไฟล์

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public void save(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir); // สร้าง path ของไฟล์ที่จะ upload

        // exists ใช้ในการตรวจสอบว่า path ที่กำหนดมีอยู่หรือไม่ 
        if (!Files.exists(uploadPath)) {
            try {
                // ถ้า path ที่กำหนดไม่มีอยู่ ให้สร้าง path นั้นขึ้นมา
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                throw new RuntimeException("Could not create directory to save file: " + uploadPath, e);
            }
        }

        // InputStream ใช้ในการอ่านข้อมูลจากไฟล์
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // resolve ใช้ในการรวม path ของไฟล์ที่จะ upload กับ path ที่กำหนด
            Path filePath = uploadPath.resolve(fileName); // สร้าง path ของไฟล์ที่จะ upload
            Files.copy(inputStream, filePath); // ทำการ copy ไฟล์ที่ upload ไปยัง path ที่กำหนด โดย Files.copy จะเป็น OutputStream ที่ใช้ในการเขียนข้อมูลลงในไฟล์

            // InputStream ใช้สำหรับ read ข้อมูลจากไฟล์ หรือ source อื่น ๆ
            // OutputStream ใช้สำหรับ write ข้อมูลลงในไฟล์ หรือ destination อื่น ๆ

            // [client] -> (upload file(InputStream)) -> [java program] -> (write file(OutputStream)) -> [file system]
        } catch (Exception e) {
            throw new RuntimeException("Could not save file: " + fileName, e);
        }
    }

    @Override
    public void remove(String uploadDir, String fileName) {
        Path fileToDelete = Paths.get(uploadDir).resolve(fileName); // สร้าง path ของไฟล์ที่จะลบ

        try {
            Files.deleteIfExists(fileToDelete); // ลบไฟล์ที่กำหนด
        } catch (Exception e) {
            throw new RuntimeException("Could not delete file: " + fileName, e);
        }
    }
}
