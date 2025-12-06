package com.example.tubesrpl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.Matkul;

import java.util.List;

@Repository
public class MatkulRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Matkul> matkulRowMapper = (rs, rowNum) -> {
        Matkul matkul = new Matkul();
        matkul.setId(rs.getLong("id"));
        matkul.setNamaMatkul(rs.getString("nama_matkul"));
        matkul.setKelasMatkul(rs.getString("kelas_matkul"));
        return matkul;
    };

    public List<Matkul> findAllByUserId(Long userId) {
        String sql = """
            SELECT m.id, m.nama_matkul, m.kelas_matkul 
            FROM matkul m 
            JOIN user_matkul um ON m.id = um.matkul_id 
            WHERE um.user_id = ?
        """;
        
        return jdbcTemplate.query(sql, matkulRowMapper, userId);
    }

    public List<Matkul> findAllBySemester(Long idSemester){
        String sql = """
            SELECT 
                m.id, 
                nama_matkul, 
                kelas_matkul
            FROM matkul m
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            WHERE ms.semester_id = ?
        """;
        
        return jdbcTemplate.query(sql, matkulRowMapper, idSemester);    
    }
}