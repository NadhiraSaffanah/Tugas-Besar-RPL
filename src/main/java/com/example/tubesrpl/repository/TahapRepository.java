package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.TahapTubes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TahapRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<TahapTubes> tahapMapper = (rs, rowNum) -> {
        TahapTubes t = new TahapTubes();
        t.setId(rs.getLong("id"));
        t.setNamaTahap(rs.getString("nama_tahap"));
        t.setDeskripsi(rs.getString("deskripsi"));
        t.setRubrikPenilaian(rs.getString("rubrik_penilaian"));
        t.setTanggalAkhir(rs.getDate("tanggal_akhir").toLocalDate());
        t.setStatusPenilaian(rs.getString("status_penilaian"));
        t.setStatusVisibility(rs.getString("status_visibility_nilai"));
        return t;
    };

    public List<TahapTubes> findAllByTubesId(Long tubesId) {
        String sql = "SELECT * FROM tahap_tubes WHERE tubes_id = ? ORDER BY tanggal_akhir ASC";
        return jdbcTemplate.query(sql, tahapMapper, tubesId);
    }
}