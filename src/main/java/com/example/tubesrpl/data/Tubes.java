package com.example.tubesrpl.data;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tubes {
    private Long id;
    // private Long idMatkul;
    // private Long idSemester; //mungkin sekalian gitu ya biar ga manggil" terus
    private LocalDate startDate;
    private LocalDate endDate;

    // yg bisa null
    private String nama;
    private String matkul;
    private String deskripsi;
    private int jml_kelompok;
}
