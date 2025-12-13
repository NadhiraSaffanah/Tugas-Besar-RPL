package com.example.tubesrpl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class PenilaianRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findNilaiByTahapId(Long tahapId) {
        String sql = "SELECT user_id, nilai, komentar FROM penilaian WHERE tahap_id = ?";
        return jdbcTemplate.queryForList(sql, tahapId);
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
            batchArgs.add(new Object[]{
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