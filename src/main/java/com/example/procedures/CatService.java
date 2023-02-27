package com.example.procedures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class CatService {
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

    @Autowired
    private JdbcTemplate template;
    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    public void init() {

        simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName("getCatById");

    }

    public int create(Cat cat) {
        return template.update("insert into cat values(?,?)", cat.getName(), cat.getType());
    }

    public Cat getById(int id) {
        SqlParameterSource in = new MapSqlParameterSource().addValue("id", id);
        Map out = simpleJdbcCall.execute(in);
        Cat cat = new Cat();
        cat.setId(id);
        cat.setName((String) out.get("name"));
        AnimalType type = new AnimalType();
        type.setId((Integer) out.get("animal_id"));
        type.setName((String) out.get("animal_name"));
        cat.setType(type);
        return cat;
    }

    public List<String> showAllProcedures() {
        List<String> sql = template.query(SHOW_PROCEDURES, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
            }
        });
        return sql;
    }
}
