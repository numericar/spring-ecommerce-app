package com.shopme.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.entities.Role;
import com.shopme.entities.User;

// UserDetails เป็น interface ที่ใช้ใน spring security โดยจะใช้จัดเก็บข้อมูลของผู้ใช้ สำหรับใช้ในระบบ security
public class ShopmeUserDetail implements UserDetails {

    private User user;

    public ShopmeUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = this.user.getRoles();
        roles.forEach((r) -> System.out.println(r.getName()));

        // SimpleGrantedAuthority เป็นคลาสใน Spring Security ใช้สำหรับจัดเก็บข้อมูลเกี่ยวกับสิทธิ์ของผู้ใช้
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

}
