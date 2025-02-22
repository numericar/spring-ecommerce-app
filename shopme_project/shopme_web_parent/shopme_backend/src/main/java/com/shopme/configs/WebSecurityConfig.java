package com.shopme.configs;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // ใช้ในการบอกว่า class นี้เป็น class ที่ใช้ในการกำหนดค่าต่าง ๆ ของ Spring application context และ Spring IoC container
@EnableWebSecurity // ใช้ในการบอกว่า class นี้จะใช้ Spring Security ในการจัดการความปลอดภัยของแอปพลิเคชัน
public class WebSecurityConfig {

    /*
     *  HttpSecurity คือ 
     *      class ที่ใช้ในการกำหนดค่าต่าง ๆ ของ Spring Security โดยที่เราสามารถกำหนดค่าต่าง ๆ ได้ เช่น 
     *      การกำหนดว่า URL ไหนที่จะต้องมีการ login ก่อนถึงจะเข้าถึงได้ หรือการกำหนดว่า URL ไหนที่จะไม่ต้อง login ก็สามารถเข้าถึงได้
     */

    // @Bean // ใช้ในการบอกว่า method นี้จะ return object ที่จะถูกจัดเก็บไว้ใน Spring container
    // SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
        
    //     http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        
    //     return http.build();
    // }

    @Bean 
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests(auth -> {
        //     auth
        //         .anyRequest() // กำหนดว่า URL ไหนที่จะต้องมีการ login ก่อนถึงจะเข้าถึงได้ ในที่นี้คือทุก URL
        //         .authenticated(); // กำหนดว่า URL ไหนที่จะต้องมีการ login ก่อนถึงจะเข้าถึงได้
        // });

        String[] resourceUrl = { "/css/**", "/js/**", "/images/**", "/webjars/bootstrap/**" };
        String[] privateUrl = { "/users/**" };
        String[] publicUrl = { "/auths/**" };

        // Stream คือ 
        String[] combinedPublicUrl = Stream.concat(Stream.of(resourceUrl), Stream.of(publicUrl)).toArray(String[]::new);

        http.authorizeHttpRequests(auth -> auth.requestMatchers(privateUrl).authenticated()); // กำหนดว่า URL ไหนที่จะต้องมีการ login ก่อนถึงจะเข้าถึงได้ ในที่นี้คือทุก URL
        http.authorizeHttpRequests(auth -> auth.requestMatchers(combinedPublicUrl).permitAll()); // กำหนดว่า URL ไหนที่จะไม่ต้อง login ก็สามารถเข้าถึงได้

        // ตั้งค่าการ login
        http.formLogin(form -> {
            form.loginPage("/auths/login"); // กำหนดว่า URL ไหนที่จะใช้ในการ login
            form.loginProcessingUrl("/auths/login"); // กำหนดว่า URL ไหนที่จะใช้ในการ login
            form.failureUrl("/auths/login?error"); // กำหนดว่า URL ไหนที่จะใช้ในกรณีที่ login ไม่สำเร็จ
            form.defaultSuccessUrl("/"); // กำหนดว่า URL ไหนที่จะใช้ในกรณีที่ login สำเร็จ
            form.usernameParameter("email"); // กำหนดชื่อของข้อมูลที่ใช้เป็น username
            form.passwordParameter("password"); // กำหนดชื่อของข้อมูลที่ใช้เป็น password
            form.permitAll(); // กำหนดว่า URL ไหนที่จะไม่ต้อง login ก็สามารถเข้าถึงได้
        });

        return http.build();
    }

    // คือ method ที่ใช้ในการ return object ที่ใช้ในการ encode รหัสผ่าน
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
