package com.example.pos.authentication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import com.example.pos.constant.JavaMessage;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "pos_user")
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    private String token;
    private String expiredToken;

    public String getExpiredToken() {
        return expiredToken;
    }

    public void setExpiredToken(String expired) {
        this.expiredToken = expired;
    }

    @Column(nullable = false)
    private String fullName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(unique = true, length = 100, nullable = false)
    @Email(message = JavaMessage.invalidEmail)
    @NotBlank(message = JavaMessage.required)
    private String email;
 
    @Column(nullable = false)
    @NotBlank(message = JavaMessage.required)
    @NotNull
    // @ValidPassword
    private String password;

    @Column(unique = true,name = "phone",length = 12)
    private String phone;

    public void setPhone(String phone){
        this.phone =phone;
    }

    public String getPhone(){
        return phone;
    }


    @Column(name = "role")
    private int role=0;
    public void setRole(int role){
        this.role=role;
    }

    public int getRole(){
        return role;
    }


    // @CreationTimestamp
    // @Column(updatable = false, name = "created_at")
    // private Date createdAt;

    // @UpdateTimestamp
    // @Column(name = "updated_at")
    // private Date updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public User setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    // public Date getCreatedAt() {
    //     return createdAt;
    // }

    // public User setCreatedAt(Date createdAt) {
    //     this.createdAt = createdAt;
    //     return this;
    // }

    // public Date getUpdatedAt() {
    //     return updatedAt;
    // }

    // public User setUpdatedAt(Date updatedAt) {
    //     this.updatedAt = updatedAt;
    //     return this;
    // }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                // ", createdAt=" + createdAt +
                // ", updatedAt=" + updatedAt +
                '}';
    }


    @Column(name = "status")
    private boolean status = true;

    @Column(name = "is_deleted")
    private boolean isDeleted=false;

    @CreationTimestamp
    @Column(updatable = false,name = "create_date")
    private Date createdDate;

    @Column(name = "create_by")
    private int createBy;
}