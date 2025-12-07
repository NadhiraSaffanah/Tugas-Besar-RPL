package com.example.tubesrpl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.Matkul;

import java.util.List;
import java.util.Map;

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

    // BARU : Method untuk mengambil Header Info (Nama Matkul + Tanggal Semester)
    public Map<String, Object> findHeaderInfo(Long matkulId) {
        String sql = """
            SELECT m.nama_matkul, s.start_date, s.end_date
            FROM matkul m
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            WHERE m.id = ?
        """;
        
        return jdbcTemplate.queryForMap(sql, matkulId);
    }
}