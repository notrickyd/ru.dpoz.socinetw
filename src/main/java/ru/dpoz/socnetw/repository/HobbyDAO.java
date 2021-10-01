package ru.dpoz.socnetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dpoz.socnetw.model.HobbyEntity;
import ru.dpoz.socnetw.repository.intf.Hobby;

import java.util.List;

@Repository("Hobbies")
public class HobbyDAO implements Hobby
{
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public int add(HobbyEntity hobby)
    {
        return 0;
    }

    @Override
    public List<HobbyEntity> getAll()
    {
        String SQL_ALL = "select hobby_id, name from hobby order by name";
        return jdbc.query(SQL_ALL, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(HobbyEntity.class));
    }
}
