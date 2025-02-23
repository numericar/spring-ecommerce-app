package com.shopme.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_email", length = 128, nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", length = 64, nullable = false)
    private String password;

    @Column(name = "user_first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "user_last_name", length = 45, nullable = false)
    private String lastName;

    @Column(name = "user_photo", length = 64)
    private String photos;

    @Column(name = "user_enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER) // สำหรับ join table ระหว่าง users กับ roles ในรูปแบบ many-to-many
    @JoinTable( // กำหนดชื่อของ join table และชื่อของคอลัมน์ที่เป็น foreign key ของทั้ง 2 ตาราง
        name = "users_roles", // ชื่อของ join table
        joinColumns = @JoinColumn(name = "user_id"), // ชื่อของคอลัมน์ที่เป็น foreign key ของตาราง users 
        inverseJoinColumns = @JoinColumn(name = "role_id")) // ชื่อของคอลัมน์ที่เป็น foreign key ของตาราง roles
    private Set<Role> roles = new HashSet<>();

    public User() {
        
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", photos=" + photos + ", enabled=" + enabled + ", roles=" + roles + "]";
    }
}
