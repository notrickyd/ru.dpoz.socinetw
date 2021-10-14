package ru.dpoz.socinetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dpoz.socinetw.helpers.SqlUtils;
import ru.dpoz.socinetw.model.UserEntity;
import ru.dpoz.socinetw.repository.intf.User;

import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("Users")
public class UserDAO implements User
{
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public UUID add(UserEntity user)
    {
        UUID newId = UUID.randomUUID();
        String SQL_ADD = "insert into users (user_id, first_name, last_name, age, gender, city) " +
                "values (F_UUID_TO_BIN(:uid), :first, :last, :age, :gender, :city)";
        jdbc.update(SQL_ADD,
                new MapSqlParameterSource()
                        .addValue("uid", newId, Types.VARCHAR)
                        .addValue("first", user.getFirstName(), Types.VARCHAR)
                        .addValue("last", user.getLastName(), Types.VARCHAR)
                        .addValue("age", user.getAge(), Types.SMALLINT)
                        .addValue("gender", user.getGender(), Types.VARCHAR)
                        .addValue("city", user.getCity(), Types.VARCHAR)
        );
        return newId;
    }

    @Override
    public List<UUID> addAll(List<UserEntity> users)
    {
        List<UUID> userIds = new ArrayList<>();
        String SQL_BULK = "insert into users (user_id, first_name, last_name, age, gender, city) VALUES";
        MapSqlParameterSource params = new MapSqlParameterSource();
        int i = 0;
        for (UserEntity u: users) {
            i++;
            SQL_BULK += MessageFormat.format("(F_UUID_TO_BIN(:uid{0}), :first{0}, :last{0}, :age{0}, :gender{0}, :city{0}),", Integer.toString(i));
            u.setUserId(UUID.randomUUID());
            userIds.add(u.getUserId());
            params
                .addValue("uid" + Integer.toString(i), u.getUserId(), Types.VARCHAR)
                .addValue("first" + Integer.toString(i), u.getFirstName(), Types.VARCHAR)
                .addValue("last" + Integer.toString(i), u.getLastName(), Types.VARCHAR)
                .addValue("age" + Integer.toString(i), u.getAge(), Types.SMALLINT)
                .addValue("gender" + Integer.toString(i), u.getGender(), Types.VARCHAR)
                .addValue("city" + Integer.toString(i), u.getCity(), Types.VARCHAR);
        }
        jdbc.update(SQL_BULK.substring(0, SQL_BULK.length() -1), params);
        return userIds;
    }

    @Override
    public int count()
    {
        String SQL_GET_COUNT = "select count(1) as cnt from users";
        return jdbc.queryForObject(
                SQL_GET_COUNT,
                new MapSqlParameterSource(),
                int.class
        );
    }

    @Override
    public List<UserEntity> getAll(Integer page)
    {
        Integer rowno = page * 100;
        String SQL_GET_ALL = "select F_BIN_TO_UUID(user_id) as user_id, age, city, first_name, gender, last_name " +
                "from users order by first_name, last_name limit " + rowno.toString() + ", 100;";
        return jdbc.query(
                SQL_GET_ALL,
                new MapSqlParameterSource(),
                new BeanPropertyRowMapper<>(UserEntity.class)
        );
    }

    @Override
    public List<UserEntity> findByName(String first, String last)
    {
        first = SqlUtils.prepareParam(first) + '%';
        last = SqlUtils.prepareParam(last) + '%';
        String SQL_FIND = "select F_BIN_TO_UUID(user_id) as user_id, age, city, first_name, gender, last_name " +
                "from users where first_name like :first and last_name like :last order by user_id";

        return jdbc.query(
                SQL_FIND,
                new MapSqlParameterSource().addValue("first", first, Types.VARCHAR).addValue("last", last, Types.VARCHAR),
                new BeanPropertyRowMapper<>(UserEntity.class)
        );
    }

    @Override
    public UserEntity get(UUID userId)
    {
        String SQL_GET = "select F_BIN_TO_UUID(user_id) as user_id, age, city, first_name, gender, last_name " +
                "from users where user_id = F_UUID_TO_BIN(:uid)";
        return jdbc.queryForObject(
                SQL_GET,
                new MapSqlParameterSource().addValue("uid", userId,  Types.VARCHAR),
                new BeanPropertyRowMapper<>(UserEntity.class)
        );
    }
}
