package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.data.Phase;
import com.example.tubesrpl.data.Tubes;

@Repository
public class PhaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Phase> phaseRowMapper = new RowMapper<Phase>() {
        @Override
        public Phase mapRow(ResultSet rs, int rowNum) throws SQLException {
            Phase phase = new Phase();
            phase.setId(rs.getLong("phase_id"));
            phase.setNama(rs.getString("nama_tahap"));
            phase.setDeskripsi(rs.getString("deskripsi"));
            phase.setRubrik_penilaian(rs.getString("rubrik_penilaian"));
            // phase.setIdMatkul(rs.getLong("matkul_id"));
            // phase.setIdSemester(rs.getLong("semester_id")); //mungkin kepake
            phase.setStatus_penilaian(rs.getString("status_penilaian"));
            phase.setIdTubes(rs.getLong("tubes_id"));
            phase.setTanggal_akhir(rs.getDate("tanggal_akhir").toLocalDate()); //convert sql.Date jadi LocalDate
            return phase;
        }
    };

    public List<Phase> findAllBySemester(Long semester){
        String sql = """
            SELECT 
                tt.id AS phase_id,
                tt.nama_tahap,
                tt.deskripsi,
                tt.rubrik_penilaian,
                tt.tanggal_akhir,
                tt.status_penilaian,
                tt.tubes_id,
                m.id,
                m.nama_matkul
                s.id
            FROM tahap_tubes tt
            JOIN tubes t ON tt.tubes_id = t.id
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            WHERE s.id = ?
        """;
        return jdbcTemplate.query(sql, phaseRowMapper, semester);
    }
}
