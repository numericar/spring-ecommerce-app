package com.shopme.services;

import java.io.IOException;
import java.util.List;

import com.shopme.entities.User;

import jakarta.servlet.http.HttpServletResponse;

public interface UserCsvExporter {
    // HttpServletResponse ใช้ในการส่งข้อมูลกลับไปยัง client โดยทั่วไปจะใช้ในการส่งไฟล์กลับไปยัง client
    void export(List<User> listUsers, HttpServletResponse response) throws IOException;
}
