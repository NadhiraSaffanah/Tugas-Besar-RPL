package com.example.tubesrpl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matkul {
    private Long id;
    private String namaMatkul;
    private String kelasMatkul;
}