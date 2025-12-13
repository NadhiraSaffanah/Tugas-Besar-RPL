package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.TahapTubes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<TahapTubes> findAllBySemester(Long semesterId) {
        String sql = """
            SELECT 
                tt.*, 
                m.nama_matkul 
            FROM tahap_tubes tt
            JOIN tubes t ON tt.tubes_id = t.id
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            WHERE ms.semester_id = ?
            ORDER BY tt.tanggal_akhir ASC
        """;
        
        RowMapper<TahapTubes> semesterMapper = (rs, rowNum) -> {
            TahapTubes t = new TahapTubes();
            t.setId(rs.getLong("id"));
            t.setNamaTahap(rs.getString("nama_tahap"));
            t.setDeskripsi(rs.getString("deskripsi"));
            t.setRubrikPenilaian(rs.getString("rubrik_penilaian"));
            t.setTanggalAkhir(rs.getDate("tanggal_akhir").toLocalDate());
            t.setStatusPenilaian(rs.getString("status_penilaian"));
            t.setStatusVisibility(rs.getString("status_visibility_nilai"));
            
            // Set Nama Matkul dari hasil Join
            t.setNamaMatkul(rs.getString("nama_matkul"));
            return t;
        };

        return jdbcTemplate.query(sql, semesterMapper, semesterId);
    }

    // bikin tahap tubes
    public void createTahap(String nama, String deskripsi, String rubrik, LocalDate tanggalAkhir, Long tubesId) {
        String sql = """
            INSERT INTO tahap_tubes 
            (nama_tahap, deskripsi, rubrik_penilaian, tanggal_akhir, tubes_id, status_penilaian, status_visibility_nilai) 
            VALUES (?, ?, ?, ?, ?, 'Not Graded', 'Hide') 
        """; 
        
        jdbcTemplate.update(sql, nama, deskripsi, rubrik, tanggalAkhir, tubesId);
    }

    public List<Map<String, Object>> findNilaiByTahapIdAndGroupId(Long tahapId, Long groupId) {
        String sql = """
            SELECT 
                u.id AS user_id, 
                u.nama, 
                u.npm,
                COALESCE(p.nilai, 0) AS nilai,
                COALESCE(p.komentar, '') AS komentar
            FROM users u
            JOIN anggota_kelompok ak ON u.id = ak.user_id
            LEFT JOIN penilaian p ON u.id = p.user_id AND p.tahap_id = ?
            WHERE ak.kelompok_id = ?
            ORDER BY u.nama ASC
        """;
        // Menggunakan queryForList karena kita ingin mendapatkan Map<String, Object>
        return jdbcTemplate.queryForList(sql, tahapId, groupId);
    }

    // update tahap tubes
    public void updateTahap(Long id, String namaTahap, String deskripsi, String rubrik, LocalDate tanggalAkhir) {
        String sql = "UPDATE tahap_tubes SET nama_tahap = ?, deskripsi = ?, rubrik_penilaian = ?, tanggal_akhir = ? WHERE id = ?";
        jdbcTemplate.update(sql, namaTahap, deskripsi, rubrik, tanggalAkhir, id);
    }

    public Optional<TahapTubes> findById(Long id) {
        String sql = "SELECT * FROM tahap_tubes WHERE id = ?";
        return jdbcTemplate.query(sql, tahapMapper, id).stream().findFirst();
    }
}