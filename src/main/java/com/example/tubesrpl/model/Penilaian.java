package com.example.tubesrpl.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Penilaian {
    private Long userId;
    private Long tahapId;
    private BigDecimal nilai;
    private String komentar;
}
