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

    // yg bisa null
    private String namaTubes;
    private String matkul;
    private String kelas; // butuh krn 1 matkul punya beberapa kelas
    private String deskripsi;
    private int jmlKelompok;
    private boolean isLocked;
    
}
