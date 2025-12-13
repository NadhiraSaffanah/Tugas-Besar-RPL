package com.example.tubesrpl.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.tubesrpl.model.Semester;

@Repository
public class SemesterRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final RowMapper<Semester> semesterRowMapper = new RowMapper<Semester>() {
        @Override
        public Semester mapRow(ResultSet rs, int rowNum) throws SQLException {
            Semester semester = new Semester();
            semester.setId(rs.getLong("id"));
            semester.setStartDate(rs.getDate("start_date").toLocalDate());
            semester.setEndDate(rs.getDate("end_date").toLocalDate()); //convert sql.Date jadi LocalDate
            semester.setJenis(rs.getString("jenis_semester"));
            return semester;
        }
    };

    public List<Semester> findAll(){
        String sql = """
            SELECT id, start_date, end_date, jenis_semester
            FROM semester
        """;
        return jdbcTemplate.query(sql, semesterRowMapper);
    }
}
