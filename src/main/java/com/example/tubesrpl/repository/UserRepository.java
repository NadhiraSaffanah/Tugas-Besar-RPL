package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.data.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Ini fungsi mapping manual (pengganti @Column)
    private static final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setNama(rs.getString("nama"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password_hash")); 
            user.setRole(rs.getString("role"));
            user.setNpm(rs.getString("npm"));
            user.setNoInduk(rs.getString("no_induk")); 
            user.setFotoProfil(rs.getString("foto_profil"));
            
            return user;
        }
    };

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email).stream().findFirst();
    }
}