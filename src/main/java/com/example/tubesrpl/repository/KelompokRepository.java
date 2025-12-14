package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.Kelompok;
import com.example.tubesrpl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class KelompokRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Kelompok> kelompokRowMapper = (rs, rowNum) -> {
        Kelompok k = new Kelompok();
        k.setId(rs.getLong("id"));
        k.setNamaKelompok(rs.getString("nama_kelompok"));
        k.setJmlAnggota(rs.getInt("jml_anggota"));
        k.setTubesId(rs.getLong("tubes_id"));
        return k;
    };

    private static final RowMapper<User> memberRowMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        return u;
    };

    public List<User> findAnggotaByKelompokId(Long kelompokId) {
        String sql = """
                    SELECT u.* FROM users u
                    JOIN anggota_kelompok ak ON u.id = ak.user_id
                    WHERE ak.kelompok_id = ?
                    ORDER BY u.nama ASC
                """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), kelompokId);
    }

    public List<Kelompok> findAllByTubesId(Long tubesId) {
        String sqlKelompok = "SELECT * FROM kelompok WHERE tubes_id = ? ORDER BY nama_kelompok ASC";
        List<Kelompok> listKelompok = jdbcTemplate.query(sqlKelompok, kelompokRowMapper, tubesId);

        for (Kelompok k : listKelompok) {
            String sqlAnggota = """
                        SELECT u.* FROM users u
                        JOIN anggota_kelompok ak ON u.id = ak.user_id
                        WHERE ak.kelompok_id = ?
                    """;

            List<User> anggotas = jdbcTemplate.query(
                    sqlAnggota,
                    new org.springframework.jdbc.core.BeanPropertyRowMapper<>(User.class),
                    k.getId());
            k.setListAnggota(anggotas);
        }

        return listKelompok;
    }

    public Kelompok findById(Long id) {
        String sql = "SELECT * FROM kelompok WHERE id = ?";
        try {
            // queryForObject digunakan kalau hasilnya pasti cuma 1 baris
            return jdbcTemplate.queryForObject(sql, kelompokRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateKelompok(Long id, String namaKelompok, int jmlAnggota) {
        String sql = "UPDATE kelompok SET nama_kelompok = ?, jml_anggota = ? WHERE id = ?";
        jdbcTemplate.update(sql, namaKelompok, jmlAnggota, id);
    }

    // cari mahasiswa yg belum masuk kelompok
    public List<User> findAvailableStudents(Long matkulId, Long tubesId) {
        String sql = """
                    SELECT u.* FROM users u
                    JOIN user_matkul um ON u.id = um.user_id
                    WHERE um.matkul_id = ?
                    AND u.id NOT IN (
                        SELECT ak.user_id
                        FROM anggota_kelompok ak
                        JOIN kelompok k ON ak.kelompok_id = k.id
                        WHERE k.tubes_id = ?
                    )
                    ORDER BY u.nama ASC
                """;

        return jdbcTemplate.query(sql, new org.springframework.jdbc.core.BeanPropertyRowMapper<>(User.class), matkulId,
                tubesId);
    }

    public void updateAnggotaKelompok(Long kelompokId, List<Long> userIds) {
        String deleteSql = "DELETE FROM anggota_kelompok WHERE kelompok_id = ?";
        jdbcTemplate.update(deleteSql, kelompokId);

        if (userIds != null && !userIds.isEmpty()) {
            String insertSql = "INSERT INTO anggota_kelompok (kelompok_id, user_id) VALUES (?, ?)";

            List<Object[]> batchArgs = new ArrayList<>();
            for (Long uid : userIds) {
                batchArgs.add(new Object[] { kelompokId, uid });
            }
            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }

    public Integer countGradedMembersByGroupAndTahap(Long kelompokId, Long tahapId) {
        String sqlCheck = """
                    SELECT COUNT(p.user_id)
                    FROM penilaian p

                    -- JOIN ke anggota_kelompok untuk menghubungkan Penilaian dengan Kelompok
                    JOIN anggota_kelompok ak ON p.user_id = ak.user_id

                    -- Filter berdasarkan ID Kelompok (dari tabel ak) dan ID Tahap (dari tabel p)
                    WHERE ak.kelompok_id = ?
                    AND p.tahap_id = ?
                """;

        return jdbcTemplate.queryForObject(sqlCheck, Integer.class, kelompokId, tahapId);
    }

    // new
    public Kelompok findKelompokByUserAndTubes(Long userId, Long tubesId) {
        String sql = """
                    SELECT k.*
                    FROM kelompok k
                    JOIN anggota_kelompok ak ON k.id = ak.kelompok_id
                    WHERE ak.user_id = ?
                    AND k.tubes_id = ?
                    LIMIT 1
                """;

        try {
            return jdbcTemplate.queryForObject(sql, kelompokRowMapper, userId, tubesId);
        } catch (Exception e) {
            return null; // user belum join kelompok
        }
    }

    // untuk save pilihan kelompok
    public void saveUserGroup(Long userId, Long groupId) {
        // hapus anggota di kelompok lama
        String deleteSql = "DELETE FROM anggota_kelompok WHERE user_id = ?";
        jdbcTemplate.update(deleteSql, userId);

        // masukin anggota ke kelompok baru
        String insertSql = "INSERT INTO anggota_kelompok (user_id, kelompok_id) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, userId, groupId);
    }

    // Untuk menghapus pilihan kelompok
    public void deleteUserGroup(Long userId) {
        String sql = "DELETE FROM anggota_kelompok WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    // Ambil kapasitas maksimal kelompok
    public int getMaxCapacityByGroupId(Long groupId) {
        String sql = "SELECT jml_anggota FROM kelompok WHERE id = ?";
        Integer capacity = jdbcTemplate.queryForObject(sql, Integer.class, groupId);
        return capacity != null ? capacity : 0;
    }

    // Ambil jumlah anggota saat ini
    public int getCurrentMembersByGroupId(Long groupId) {
        String sql = "SELECT COUNT(*) FROM anggota_kelompok WHERE kelompok_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, groupId);
        return count != null ? count : 0;
    }

    // untuk ngecek apakah udh join kelompok atau belom
    public boolean isUserAlreadyInAnyGroup(Long userId, Long matkulId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM anggota_kelompok ak
                    JOIN kelompok k ON ak.kelompok_id = k.id
                    JOIN tubes t ON k.tubes_id = t.id
                    WHERE ak.user_id = ? AND t.matkul_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, matkulId);
        return count != null && count > 0;
    }

    public Long findMatkulIdByUserId(Long userId) {
        // Mencari Matkul ID yang terkait dengan kelompok yang dimiliki user
        String sql = """
                    SELECT t.matkul_id
                    FROM anggota_kelompok ak
                    JOIN kelompok k ON ak.kelompok_id = k.id
                    JOIN tubes t ON k.tubes_id = t.id
                    WHERE ak.user_id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}