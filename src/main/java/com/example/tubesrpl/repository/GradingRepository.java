package com.example.tubesrpl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.tubesrpl.model.Grading;

@Repository
public class GradingRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Grading> gradingRowMapper = (rs, rowNum) -> {
        Grading grading = new Grading();

        grading.setUserId(rs.getLong("user_id"));
        grading.setTahapId(rs.getLong("tahap_id"));
        grading.setNilai(rs.getBigDecimal("nilai"));
        grading.setKomentar(rs.getString("komentar"));
        
        return grading;
    };

    /**
     * Mengambil daftar semua penilaian berdasarkan ID Tahap Tubes.
     */
    public List<Grading> findAllByTahapId(Long tahapId) {
        String sql = "SELECT user_id, tahap_id, nilai, komentar FROM penilaian WHERE tahap_id = ?";
        return jdbcTemplate.query(sql, gradingRowMapper, tahapId);
    }

    /**
     * Mengambil satu entri penilaian berdasarkan user dan tahap ID.
     */
    public Optional<Grading> findById(Long userId, Long tahapId) {
        String sql = "SELECT user_id, tahap_id, nilai, komentar FROM penilaian WHERE user_id = ? AND tahap_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, gradingRowMapper, userId, tahapId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
