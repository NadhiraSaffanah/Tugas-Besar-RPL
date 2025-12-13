package com.example.tubesrpl.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.tubesrpl.model.Group;

@Repository
public class GroupRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Group> groupRowMapper = (rs, rowNum) -> {
        Group group = new Group();

        group.setKelompokId(rs.getLong("kelompokId"));
        group.setNama_kelompok(rs.getString("nama_kelompok"));
        group.setJml_kelompok(rs.getLong("jml_kelompok"));
        group.setTubesId(rs.getLong("tubesId"));

        return group;
    };

    /**
     * Mengambil daftar semua kelompok berdasarkan ID Tubes.
     */
    public List<Group> findByTubesId(Long tubesId) {
        String sql = "SELECT id, nama_kelompok, jml_anggota, tubes_id FROM kelompok WHERE tubes_id = ?";
        return jdbcTemplate.query(sql, groupRowMapper, tubesId);
    }

    /**
     * Mengambil satu kelompok berdasarkan ID-nya.
     */
    public Optional<Group> findById(Long id) {
        String sql = "SELECT id, nama_kelompok, jml_anggota, tubes_id FROM kelompok WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, groupRowMapper, id));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Group> findKelompokDetailByUserIdAndTubesId(Long userId, Long tubesId) {
        String sql = """
            SELECT 
                k.id, k.nama_kelompok, k.jml_anggota, k.tubes_id
            FROM 
                kelompok k
            JOIN 
                anggota_kelompok ak ON k.id = ak.kelompok_id
            WHERE 
                ak.user_id = ? AND k.tubes_id = ?
        """;
        
        try {
            Group group = jdbcTemplate.queryForObject(
                sql, 
                groupRowMapper, 
                userId, 
                tubesId
            );
            return Optional.ofNullable(group);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
