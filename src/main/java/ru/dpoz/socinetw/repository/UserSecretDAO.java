package ru.dpoz.socinetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dpoz.socinetw.config.WebSecurityConfig;
import ru.dpoz.socinetw.model.UserSecretEntity;
import ru.dpoz.socinetw.repository.intf.UserSecret;

import java.sql.Types;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Repository("UserSecret")
public class UserSecretDAO implements UserSecret
{
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public UserSecretEntity findUsername(String username)
    {
        //TODO добавить Limit 1 после переезда на mySQL
        String SQL_LOGIN_EXISTS = "select password, username, user_id from user_secret where username = :l";
        try {
            return jdbc.queryForObject(
                    SQL_LOGIN_EXISTS,
                    new MapSqlParameterSource().addValue("l", username, Types.VARCHAR),
                    new BeanPropertyRowMapper<>(UserSecretEntity.class)
            );
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void add(String username, String password, UUID userId)
    {
        String SQL_ADD = "insert into user_secret (username, password, user_id) values (:l, :p, :uid);";
        password = WebSecurityConfig.passwordEncoder().encode(password);
        jdbc.update(SQL_ADD, new MapSqlParameterSource()
                .addValue("l", username, Types.VARCHAR)
                .addValue("p", password, Types.VARCHAR)
                .addValue("uid", userId,  Types.VARCHAR)
        );
    }

    @Override
    public void add(UserSecretEntity userSecret)
    {
        this.add(userSecret.getUsername(),
                userSecret.getPassword(),
                userSecret.getUserId());
    }

    @Override
    public void addAll(List<UserSecretEntity> userSecrets)
    {
        String SQL_BULK = "insert into user_secret (username, password, user_id) VALUES ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        int i = 0;
        for (UserSecretEntity us: userSecrets) {
            i++;
            SQL_BULK += MessageFormat.format("(:l{0}, :p{0}, :uid{0}),", Integer.toString(i));
            params
                    .addValue("uid" + Integer.toString(i), us.getUserId(), Types.VARCHAR)
                    .addValue("l" + Integer.toString(i), us.getUsername(), Types.VARCHAR)
//                    .addValue("p" + Integer.toString(i), WebSecurityConfig.passwordEncoder().encode(us.getPassword()), Types.VARCHAR);
                    .addValue("p" + Integer.toString(i), us.getPassword(), Types.VARCHAR);
        }
        jdbc.update(SQL_BULK.substring(0, SQL_BULK.length() -1), params);
    }
}
