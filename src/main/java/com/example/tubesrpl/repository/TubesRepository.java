package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.data.Tubes;

@Repository
public class TubesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Tubes> tubesRowMapper = new RowMapper<Tubes>() {
        @Override
        public Tubes mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tubes tubes = new Tubes();
            tubes.setId(rs.getLong("tubes_id"));
            tubes.setNama(rs.getString("nama_tubes"));
            tubes.setDeskripsi(rs.getString("deskripsi"));
            tubes.setJml_kelompok(rs.getInt("jml_kelompok"));
            // tubes.setIdMatkul(rs.getLong("matkul_id"));
            // tubes.setIdSemester(rs.getLong("semester_id")); //mungkin kepake
            tubes.setMatkul(rs.getString("nama_matkul"));
            tubes.setStartDate(rs.getDate("start_date").toLocalDate());
            tubes.setEndDate(rs.getDate("end_date").toLocalDate()); //convert sql.Date jadi LocalDate
            return tubes;
        }
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
}
