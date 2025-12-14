// BARU DITAMBAHIN, BUAT TAHAP TUBES
package com.example.tubesrpl.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TahapTubes {
    private Long id;
    private String namaTahap;
    private String deskripsi;
    private String rubrikPenilaian;
    private LocalDate tanggalAkhir;
    private String statusPenilaian;
    private String statusVisibility;

    // Field Tambahan (Hasil Join Query)
    private String namaMatkul;
    private LocalDate semesterStart;
    private LocalDate semesterEnd;

    // === FIELD TAMBAHAN (BUAT VIEW) ===
    private Integer nilaiTahap;       // dari penilaian
}