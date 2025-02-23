package com.shopme.configs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.entities.User;
import com.shopme.repositories.UserRepository;

// UserDetailsService เป็น interface ของ Spring Security ที่ใช้ในการจัดการดึงข้อมูลของผู้ใช้จากแหล่งข้อมูลต่าง ๆ เช่น database
public class ShopmeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Could not find user with email: " + email);
        }

        return new ShopmeUserDetail(user.get());
    }

}
