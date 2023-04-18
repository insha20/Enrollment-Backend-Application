package com.springboot.Backend.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "enrollUser")
public class EnrollUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String idNumber;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String photo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public EnrollUser() {
    }

    public EnrollUser(Long id, String username, String idNumber, String photo, User createdBy) {
        this.id = id;
        this.username = username;
        this.idNumber = idNumber;
        this.photo = photo;
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "EnrollUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", photo='" + photo + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}
