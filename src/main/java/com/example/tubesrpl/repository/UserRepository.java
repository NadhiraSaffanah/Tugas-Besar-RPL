package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.User;

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

    // Find users by role
    public List<User> findByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ? AND isActive = TRUE ORDER BY nama";
        return jdbcTemplate.query(sql, userRowMapper, role);
    }

    // Search users by name or email (for a specific role)
    public List<User> searchUsers(String role, String searchTerm) {
        String sql = """
            SELECT * FROM users 
            WHERE role = ? AND isActive = TRUE 
            AND (LOWER(nama) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?))
            ORDER BY nama
            LIMIT 20
        """;
        String searchPattern = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, userRowMapper, role, searchPattern, searchPattern);
    }

    // Get users linked to a matkul
    public List<User> findUsersByMatkulId(Long matkulId) {
        String sql = """
            SELECT u.* FROM users u
            JOIN user_matkul um ON u.id = um.user_id
            WHERE um.matkul_id = ? AND u.isActive = TRUE
            ORDER BY u.nama
        """;
        return jdbcTemplate.query(sql, userRowMapper, matkulId);
    }

    // Link user to matkul
    public void linkUserToMatkul(Long userId, Long matkulId) {
        String sql = """
            INSERT INTO user_matkul (user_id, matkul_id)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
        """;
        jdbcTemplate.update(sql, userId, matkulId);
    }

    // Remove user from matkul
    public void unlinkUserFromMatkul(Long userId, Long matkulId) {
        String sql = "DELETE FROM user_matkul WHERE user_id = ? AND matkul_id = ?";
        jdbcTemplate.update(sql, userId, matkulId);
    }
}