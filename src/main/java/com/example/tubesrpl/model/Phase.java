package com.example.tubesrpl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phase {
    //non null
    private Long id;
    private String nama;
    private String rubrik_penilaian;
    private String status_penilaian;
    private Long idTubes;

    //bisa null
    private String deskripsi;
    private LocalDate endDate;
    private String matkul;
}
