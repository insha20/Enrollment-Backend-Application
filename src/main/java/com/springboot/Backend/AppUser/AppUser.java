package com.springboot.Backend.AppUser;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollUser")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String idNumber;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String photo;
}
