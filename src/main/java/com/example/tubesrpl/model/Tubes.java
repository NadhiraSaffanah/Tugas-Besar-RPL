package com.example.tubesrpl.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tubes {
    private Long id;
    private Long idMatkul; // butuh buat course-details
    // private Long idSemester; //mungkin sekalian gitu ya biar ga manggil" terus
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusKelompok;

    // yg bisa null
    private String namaTubes; // ini diupdate juga biar lebih jelas (nama -> namaTubes)
    private String matkul;
    private String deskripsi;
    // --- UBAH DARI jml_kelompok MENJADI jmlKelompok ---
    private int jmlKelompok;
}