package com.example.tubesrpl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.example.tubesrpl.model.Penilaian;
import com.example.tubesrpl.model.Semester;

import org.springframework.jdbc.core.RowMapper;

@Repository
public class PenilaianRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Penilaian> penilaianRowMapper = new RowMapper<Penilaian>() {
        @Override
        public Penilaian mapRow(ResultSet rs, int rowNum) throws SQLException {
            Penilaian penilaian = new Penilaian();
            penilaian.setUserId(rs.getLong("user_id"));
            penilaian.setTahapId(rs.getLong("tahap_id"));

            BigDecimal nilai = rs.getBigDecimal("nilai");
            if (rs.wasNull()) {
                penilaian.setNilai(null);
            } else {
                penilaian.setNilai(nilai);
            }

            penilaian.setKomentar(rs.getString("komentar"));
            return penilaian;
        }
    };

    public List<Map<String, Object>> findNilaiByTahapId(Long tahapId) {
        String sql = "SELECT user_id, nilai, komentar FROM penilaian WHERE tahap_id = ?";
        return jdbcTemplate.queryForList(sql, tahapId);
    }

    // ambil nilai berdasarkan user id dan tahap tubes id
    public Penilaian findNilaiByUserIdAndTahapId(Long userId, Long tahapId) {
        String sql = """
                    SELECT user_id, tahap_id, nilai, komentar
                    FROM penilaian
                    WHERE user_id = ? AND tahap_id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, penilaianRowMapper, userId, tahapId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void saveNilaiBatch(Long tahapId, List<Long> userIds, List<Double> nilaiList, String komentar) {

        String upsertSql = """
                    INSERT INTO penilaian (nilai, komentar, tahap_id, user_id)
                    VALUES (?, ?, ?, ?)
                    ON CONFLICT (user_id, tahap_id) DO UPDATE
                    SET nilai = EXCLUDED.nilai,
                        komentar = EXCLUDED.komentar;
                """;

        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 0; i < userIds.size(); i++) {
            batchArgs.add(new Object[] {
                    nilaiList.get(i),
                    komentar,
                    tahapId,
                    userIds.get(i)
            });
        }

        jdbcTemplate.batchUpdate(upsertSql, batchArgs);

        // Update status tahap menjadi 'Graded' setelah penilaian
        jdbcTemplate.update("UPDATE tahap_tubes SET status_penilaian = 'Graded' WHERE id = ?", tahapId);
    }
}