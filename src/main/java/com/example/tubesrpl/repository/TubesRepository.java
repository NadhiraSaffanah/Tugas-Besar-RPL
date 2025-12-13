package com.example.tubesrpl.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        tubes.setLocked(rs.getBoolean("is_locked"));
        
        return tubes;
    };

    public List<Tubes> findAllBySemester(Long semester){
        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.status_kelompok,
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
                t.is_locked,
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

    public Optional<Tubes> findAllByMatkulId(Long matkulId) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findByMatkulId'");

        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.status_kelompok,
                t.matkul_id,
                t.is_locked,
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
        
        return jdbcTemplate.query(sql, tubesRowMapper, matkulId).stream().findFirst();
    }

    public Optional<Tubes> findById(Long id) {
        String sql = """
            SELECT 
                t.id AS tubes_id,
                t.nama_tubes,
                t.deskripsi,
                t.jml_kelompok,
                t.matkul_id,
                m.nama_matkul,
                t.is_locked,
                m.kelas_matkul,
                s.start_date,
                s.end_date
            FROM tubes t
            JOIN matkul m ON t.matkul_id = m.id
            JOIN matkul_semester ms ON m.id = ms.matkul_id
            JOIN semester s ON ms.semester_id = s.id
            WHERE t.id = ?
        """;
        
        // Menggunakan stream().findFirst() untuk mengembalikan Optional
        return jdbcTemplate.query(sql, tubesRowMapper, id).stream().findFirst();
    }

    private String generateGroupName(int index) {
        return "Kelompok " + (index + 1);
    }

    public void updateTubes(Long id, String namaTubes, String deskripsi, int newJmlKelompok) {
        String sqlUpdate = "UPDATE tubes SET nama_tubes = ?, deskripsi = ?, jml_kelompok = ? WHERE id = ?";
        jdbcTemplate.update(sqlUpdate, namaTubes, deskripsi, newJmlKelompok, id);

        String sqlInsert = """
            INSERT INTO kelompok (nama_kelompok, jml_anggota, tubes_id) 
            VALUES (?, ?, ?) 
            ON CONFLICT (nama_kelompok, tubes_id) DO NOTHING
        """;
        
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < newJmlKelompok; i++) {
            String namaGroup = generateGroupName(i); // Kelompok 1, Kelompok 2...
            batchArgs.add(new Object[]{namaGroup, 5, id}); // Default 5
        }

        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sqlInsert, batchArgs);
        }

    }

    public void createTubes(String namaTubes, String deskripsi, int jmlKelompok, Long matkulId) {
        String sqlTubes = "INSERT INTO tubes (nama_tubes, deskripsi, jml_kelompok, matkul_id) VALUES (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlTubes, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, namaTubes);
            ps.setString(2, deskripsi);
            ps.setInt(3, jmlKelompok);
            ps.setLong(4, matkulId);
            return ps;
        }, keyHolder);

        Long newTubesId = (Long) keyHolder.getKeys().get("id");

        if (newTubesId != null && jmlKelompok > 0) {
            String sqlKelompok = "INSERT INTO kelompok (nama_kelompok, jml_anggota, tubes_id) VALUES (?, ?, ?)";
            
            List<Object[]> batchArgs = new ArrayList<>();
            for (int i = 0; i < jmlKelompok; i++) {
                String namaGroup = generateGroupName(i);
                
                batchArgs.add(new Object[]{namaGroup, 5, newTubesId});
            }
            
            jdbcTemplate.batchUpdate(sqlKelompok, batchArgs);
        }
    }

    // BARU (buat lock/unlock kelompok)
    public void updateLockStatus(Long tubesId, boolean isLocked) {
        String sql = "UPDATE tubes SET is_locked = ? WHERE id = ?";
        jdbcTemplate.update(sql, isLocked, tubesId);
    }

    // BARU, bikin kelompok otomatis kalau belum sebanyak yang di Tubes
    public void syncGroupCount(Long tubesId, int targetCount) {
        // 1. Cek ada berapa kelompok sekarang di DB?
        String sqlCount = "SELECT COUNT(*) FROM kelompok WHERE tubes_id = ?";
        Integer currentCount = jdbcTemplate.queryForObject(sqlCount, Integer.class, tubesId);
        if (currentCount == null) currentCount = 0;

        // 2. Kalau jumlahnya KURANG dari target, kita buat sisanya
        if (currentCount < targetCount) {
            int needed = targetCount - currentCount;
            
            // Ambil semua nama yang sudah ada biar gak error UNIQUE
            String sqlExisting = "SELECT nama_kelompok FROM kelompok WHERE tubes_id = ?";
            List<String> existingList = jdbcTemplate.queryForList(sqlExisting, String.class, tubesId);
            
            List<Object[]> batchArgs = new ArrayList<>();
            int added = 0;
            int i = 1; // Mulai coba dari "Kelompok 1"

            // Loop sampai kita dapat jumlah yang dibutuhkan
            while (added < needed) {
                String candidateName = "Kelompok " + i;
                
                // Kalau nama "Kelompok 1" belum ada, berarti kita bisa pakai
                // Kalau "Kelompok 1" sudah ada, kita skip dan coba "Kelompok 2", dst.
                if (!existingList.contains(candidateName)) {
                    batchArgs.add(new Object[]{candidateName, 5, tubesId}); // Default kuota 5
                    added++;
                }
                i++;
            }

            // Eksekusi Insert sekaligus
            if (!batchArgs.isEmpty()) {
                String sqlInsert = "INSERT INTO kelompok (nama_kelompok, jml_anggota, tubes_id) VALUES (?, ?, ?)";
                jdbcTemplate.batchUpdate(sqlInsert, batchArgs);
            }
        }
    }
}

