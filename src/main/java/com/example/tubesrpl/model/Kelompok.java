package com.example.tubesrpl.model;

import lombok.Data;
import java.util.List; 
import java.util.ArrayList; 

@Data
public class Kelompok {
    private Long id;
    private String namaKelompok; 
    private int jmlAnggota;     
    private Long tubesId;       

    private List<User> listAnggota = new ArrayList<>(); 

    // tambahan
    private Integer gradedCount;
}