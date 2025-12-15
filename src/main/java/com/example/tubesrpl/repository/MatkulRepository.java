package com.example.tubesrpl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.User;

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

    //note: ini nanti harusnya ambil matkul yang aktif doang

    public List<Matkul> findAllByUserId(Long userId) {
        String sql = """
            SELECT m.id, m.nama_matkul, m.kelas_matkul 
            FROM matkul m 
            JOIN user_matkul um ON m.id = um.matkul_id 
            WHERE um.user_id = ?
        """;
        
        return jdbcTemplate.query(sql, matkulRowMapper, userId);
    }

    public List<Matkul> findAllBySemester(Long idSemester){ //homepage admin masi pake by semester
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

    public Matkul findById(Long id) {
        String sql = """
            SELECT id, nama_matkul, kelas_matkul
            FROM matkul
            WHERE id = ? AND isActive = TRUE
        """;
        List<Matkul> results = jdbcTemplate.query(sql, matkulRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
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

    // Create a new matkul
    public Long createMatkul(String namaMatkul, String kelasMatkul) {
        String sql = """
            INSERT INTO matkul (nama_matkul, kelas_matkul, isActive)
            VALUES (?, ?, TRUE)
            RETURNING id
        """;
        
        return jdbcTemplate.queryForObject(sql, Long.class, namaMatkul, kelasMatkul);
    }

    // Link matkul to semester
    public void linkMatkulToSemester(Long matkulId, Long semesterId) {
        String sql = """
            INSERT INTO matkul_semester (matkul_id, semester_id)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;
        
        jdbcTemplate.update(sql, matkulId, semesterId);
    }

    // Soft delete matkul (set isActive = FALSE)
    public void deleteMatkul(Long id) {
        String sql = "UPDATE matkul SET isActive = FALSE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<User> findParticipantsByMatkulId(Long matkulId) {
        String sql = """
            SELECT u.id, u.nama, u.email, u.role, u.foto_profil
            FROM users u
            JOIN user_matkul um ON u.id = um.user_id
            WHERE um.matkul_id = ?
            ORDER BY u.role ASC, u.nama ASC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setNama(rs.getString("nama"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            user.setFotoProfil(rs.getString("foto_profil")); 
            
            return user;
        }, matkulId);
    }

}