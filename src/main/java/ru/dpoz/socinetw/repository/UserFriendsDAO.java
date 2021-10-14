package ru.dpoz.socinetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.dpoz.socinetw.model.UserEntity;
import ru.dpoz.socinetw.repository.intf.UserFriends;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Repository("UserFriends")
public class UserFriendsDAO implements UserFriends
{
    @Autowired
    NamedParameterJdbcTemplate jdbc;

    @Override
    public boolean add(UUID userId, UUID friendId)
    {
        try {
            String SQL_ADD = "insert into user_friends(user_id, friend_id) values(F_UUID_TO_BIN(:uid), F_UUID_TO_BIN(:fid))";
            jdbc.update(SQL_ADD,
                    new MapSqlParameterSource()
                            .addValue("uid", userId,  Types.VARCHAR)
                            .addValue("fid", friendId,  Types.VARCHAR)
            );
            return true;
        }catch (DuplicateKeyException ex) {
            return false;
        }
    }

    @Override
    public void remove(UUID userId, UUID friendId)
    {
        String SQL_REMOVE = "delete from user_friends where user_id = F_UUID_TO_BIN(:uid) and friend_id = F_UUID_TO_BIN(:fid)";
        jdbc.update(SQL_REMOVE,
                new MapSqlParameterSource()
                        .addValue("uid", userId,  Types.VARCHAR)
                        .addValue("fid", friendId,  Types.VARCHAR)
        );
    }

    @Override
    public List<UUID> get(UUID userId)
    {
        String SQL_FRIENDS = "select F_BIN_TO_UUID(friend_id) as friend_id from user_friends where user_id = F_UUID_TO_BIN(:uid)";
        return jdbc.queryForList(
                SQL_FRIENDS,
                new MapSqlParameterSource().addValue("uid", userId,  Types.VARCHAR),
                UUID.class
                );
    }

    @Override
    public List<UserEntity> getUsers(UUID userId)
    {
        String SQL_FRIENDS =
                "select u.age, u.city, u.first_name, u.gender, u.last_name, u.age, u.city, u.first_name, u.gender," +
                        " u.last_name, u.id, F_BIN_TO_UUID(u.user_id) as user_id from user_friends uf " +
                "   inner join users u on u.user_id = uf.friend_id " +
                "where uf.user_id = F_UUID_TO_BIN(:uid)";
        return jdbc.query(
                SQL_FRIENDS,
                new MapSqlParameterSource().addValue("uid", userId,  Types.VARCHAR),
                new BeanPropertyRowMapper<>(UserEntity.class)
        );
    }

}
