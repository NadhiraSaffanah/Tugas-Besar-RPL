package com.example.tubesrpl.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group {
    private long kelompokId;
    private String nama_kelompok;
    private long jml_kelompok;
    private long tubesId;

}
