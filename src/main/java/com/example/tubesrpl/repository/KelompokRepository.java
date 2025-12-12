package com.example.tubesrpl.repository;

import com.example.tubesrpl.model.Kelompok;
import com.example.tubesrpl.model.User; // Pastikan import User ada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
                k.getId()
            );
            k.setListAnggota(anggotas);
        }

        return listKelompok;
    }
}