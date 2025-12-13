package com.example.tubesrpl.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String jenis;
}
