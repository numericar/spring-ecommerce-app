package com.shopme.admin.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodedrTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = "12345";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodedPassword);

        boolean isMatches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertTrue(isMatches);
    }

}
