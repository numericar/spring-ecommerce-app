package com.shopme.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.entities.User;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserCsvExporterImpl implements UserCsvExporter {

    @Override
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        // SimpleDateFormat คือ class ที่ใช้ในการจัดรูปแบบของวันที่และเวลา
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "users_" + timestamp + ".csv";

        response.setContentType("text/csv"); // ระบุว่าเป็นไฟล์ csv

        // Content-Disposition คือ header ที่ใช้ในการระบุว่าเป็นไฟล์แบบไหน
        String headerKey = "Content-Disposition"; // ระบุว่าเป็นไฟล์แบบไหน
        String headerValue = "attachment; filename=" + fileName; // ระบุว่าเป็นไฟล์แนบ
        response.setHeader(headerKey, headerValue);

        // response.getWriter() ใช้ในการเขียนข้อมูลกลับไปยัง client
        // svPreference.STANDARD_PREFERENCE คือ การกำหนดค่าเริ่มต้นของการสร้างไฟล์ csv
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = { "User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled" };
        String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };

        // writeHeader() ใช้ในการเขียน header ของไฟล์ csv
        csvWriter.writeHeader(csvHeader);

        // write() ใช้ในการเขียนข้อมูลลงไฟล์ csv
        for (User user : listUsers) {
            csvWriter.write(user, fieldMapping); // ระบุว่าจะเขียนข้อมูลของ user ลงไฟล์ csv โดยใช้ fieldMapping ที่กำหนดไว้
        }

        csvWriter.close();
    }

}
