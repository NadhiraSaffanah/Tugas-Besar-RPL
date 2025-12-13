package com.example.tubesrpl.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Grading {
    //id: gabungan fk antara user dengan tahap
    //data user
    private Long userId; 

    //data tahaptubes
    private Long tahapId;

    //data penilaian
    private BigDecimal nilai;
    private String komentar;
    
}
