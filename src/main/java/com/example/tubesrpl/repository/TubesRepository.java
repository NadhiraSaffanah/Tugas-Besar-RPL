package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.Tubes; 

@Repository
public class TubesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Tubes> tubesRowMapper = (rs, rowNum) -> {
        Tubes tubes = new Tubes();
        tubes.setId(rs.getLong("tubes_id")); 
        tubes.setNamaTubes(rs.getString("nama_tubes"));
        tubes.setDeskripsi(rs.getString("deskripsi"));
        tubes.setJmlKelompok(rs.getInt("jml_kelompok"));
        tubes.setIdMatkul(rs.getLong("matkul_id")); // butuh buat course-details
        // tubes.setIdSemester(rs.getLong("semester_id")); //mungkin kepake
        tubes.setMatkul(rs.getString("nama_matkul"));
        tubes.setKelas(rs.getString("kelas_matkul"));
        tubes.setStartDate(rs.getDate("start_date").toLocalDate());
        tubes.setEndDate(rs.getDate("end_date").toLocalDate()); //convert sql.Date jadi LocalDate
        
        return tubes;
    };

    public List<Tubes> findAllBySemester(Long semester){
        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.matkul_id,
                ms.semester_id,
                m.nama_matkul, 
                m.kelas_matkul,
                s.start_date,
                s.end_date
            FROM tubes t
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            WHERE s.id = ?
        """;
        return jdbcTemplate.query(sql, tubesRowMapper, semester);
    }

    //NEW
    public List<Tubes> findAllByUserId(Long id){
        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.matkul_id,
                m.nama_matkul,
                m.kelas_matkul,
                s.start_date,
                s.end_date
            FROM tubes t
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            JOIN user_matkul um ON t.matkul_id = um.matkul_id
            WHERE um.user_id = ?
        """;
        return jdbcTemplate.query(sql, tubesRowMapper, id);
    }

    // BARU 
    public Optional<Tubes> findByMatkulId(Long matkulId) {
        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.matkul_id,
                m.nama_matkul,
                m.kelas_matkul,
                s.start_date,
                s.end_date
            FROM tubes t
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            WHERE t.matkul_id = ?
        """;
        
        // Pakai stream().findFirst() buat balikin satu data (Optional)
        return jdbcTemplate.query(sql, tubesRowMapper, matkulId).stream().findFirst();
    }

    // BARU, buat bikin tubes
    public void createTubes(String namaTubes, String deskripsi, int jmlKelompok, Long matkulId) {
        String sql = "INSERT INTO tubes (nama_tubes, deskripsi, jml_kelompok, matkul_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, namaTubes, deskripsi, jmlKelompok, matkulId);
    }

    // BARU (buat update tubes, dipanggil di DosenController)
    public void updateTubes(Long id, String namaTubes, String deskripsi, int jmlKelompok) {
        String sql = "UPDATE tubes SET nama_tubes = ?, deskripsi = ?, jml_kelompok = ? WHERE id = ?";
        jdbcTemplate.update(sql, namaTubes, deskripsi, jmlKelompok, id);
    }
}

