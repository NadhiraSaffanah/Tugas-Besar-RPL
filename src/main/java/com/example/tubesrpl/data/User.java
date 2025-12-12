package com.example.tubesrpl.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // buat mapping kolom postgre

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String role;
    private String nama;

    // yg bisa null
    private String npm;
    private String noInduk;
    private String fotoProfil;
}
