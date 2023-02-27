package com.example.procedures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class CatServiceTest {
    @Autowired
    private JdbcTemplate template;
    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    public void init() {

        simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName("getCatById");

    }

    private static final String CREATE_STORED_PROC = "DROP PROCEDURE IF EXISTS testdb.getCatById;" +
            " DELIMITER $$" +
            " $$" +
            " CREATE PROCEDURE testdb.getCatById(\n" +
            " in id integer,\n" +
            " out name varchar(20),\n" +
            " out animal_id integer,\n" +
            " out animal_name varchar(20)\n" +
            ") " +
            " begin" +
            " select c.name, a.id,a.Name into name,animal_id,animal_name from cat c join animaltype a on c.typeId = a.id\n" +
            "    where c.id = id;\n" +
            "END$$" +
            " DELIMITER;";
    private static final String SHOW_PROCEDURES = "SHOW PROCEDURE STATUS WHERE db = 'testdb';";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void showAllProcedures() {
        List<String> sql = template.query(SHOW_PROCEDURES, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return (rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }
        });
        sql.forEach(System.out::println);
    }
}