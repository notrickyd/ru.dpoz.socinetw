package ru.dpoz.socinetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dpoz.socinetw.model.HobbyEntity;
import ru.dpoz.socinetw.model.UserHobbiesEntity;
import ru.dpoz.socinetw.repository.intf.UserHobbies;

import java.sql.Types;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Repository("UsersHobbies")
public class UserHobbiesDAO implements UserHobbies
{
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public void add(UUID userId, int hobbyId)
    {
        String SQL_ADD = "insert into user_hobbies (user_id, hobby_id) " +
                "values (UNHEX(CONCAT(REPLACE(:uid, '-', ''))), :hid);";
        jdbc.update(SQL_ADD, new MapSqlParameterSource()
                .addValue("uid", userId,  Types.VARCHAR)
                .addValue("hid", hobbyId, Types.SMALLINT)
        );
    }

    @Override
    public void addAll(List<UserHobbiesEntity> usersHobbies)
    {
        String SQL_BULK = "insert into user_hobbies (user_id, hobby_id) VALUES ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        int i = 0;
        for (UserHobbiesEntity uh: usersHobbies) {
            i++;
            SQL_BULK += MessageFormat.format("(UNHEX(CONCAT(REPLACE(:uid{0}, '-', ''))), :h{0}),", Integer.toString(i));
            params
                    .addValue("uid" + Integer.toString(i), uh.getUserId(), Types.VARCHAR)
                    .addValue("h" + Integer.toString(i), uh.getHobbyId(), Types.VARCHAR);
        }
        jdbc.update(SQL_BULK.substring(0, SQL_BULK.length() -1), params);
    }

    @Override
    public List<HobbyEntity> get(UUID userId)
    {
        String SQL_GET = "select h.* from user_hobbies uh " +
                "inner join hobby h on h.hobby_id = uh.hobby_id " +
                "where uh.user_id = UNHEX(CONCAT(REPLACE(:uid, '-', '')))";
        return jdbc.query(
                SQL_GET,
                new MapSqlParameterSource().addValue("uid", userId,  Types.VARCHAR),
                new BeanPropertyRowMapper<>(HobbyEntity.class)
                );
    }
}
