package com.shopme.configs;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// WebMvcConfigurer คือ interface ที่ใช้ในการกำหนดค่าต่าง ๆ ของ Spring MVC
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // addResourceHandlers ใช้สำหรับกำหนดเส้นทางของ static resource เช่น ไฟล์รูปภาพ, css, js ซึ่งจะถูก serve โดย Spring MVC
    // ResourceHandlerRegistry ใช้สำหรับกำหนดเส้นทางของ static resource เช่น ไฟล์รูปภาพ, css, js ซึ่งจะถูก serve โดย Spring MVC
    // โดยที่ addResourceHandler คือ path ที่จะใช้ในการเข้าถึงไฟล์ และ addResourceLocations คือ path ที่เก็บไฟล์
    // ในที่นี้เรากำหนด path ที่จะ upload ไฟล์ ให้เป็น user-photos และ path ที่เก็บไฟล์ที่ upload ไว้ใน userPhotoPath
    // โดยที่ userPhotoPath คือ path ที่เข้าถึงได้ โดย absolutePath คือ path ที่เข้าถึงได้จริง
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "userPhotos"; // กำหนดชื่อ directory ที่จะใช้ในการ upload ไฟล์
        Path userPhotoDir = Paths.get(dirName); // สร้าง path ของไฟล์ที่จะ upload
        String userPhotoPath = userPhotoDir.toFile().getAbsolutePath(); // แปลง path ของไฟล์ที่จะ upload ให้เป็น path ที่เข้าถึงได้ โดย absolutePath คือ path ที่เข้าถึงได้จริง

        String resourceHandlerPath = "/" + dirName + "/**"; // กำหนด path ที่จะใช้ในการเข้าถึงไฟล์
        String resourceLocationPath = "file:" + userPhotoPath + "/"; // กำหนด path ที่เก็บไฟล์ที่ upload ไว้ใน userPhotoPath

        System.out.println(resourceHandlerPath);
        System.out.println(userPhotoPath);
        System.out.println(resourceLocationPath);

        registry.addResourceHandler(resourceHandlerPath).addResourceLocations(resourceLocationPath);
    }

}
