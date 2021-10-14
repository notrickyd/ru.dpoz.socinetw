package ru.dpoz.socinetw.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.DateUtils;
import ru.dpoz.socinetw.repository.intf.UserTokens;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.UUID;

@Repository("UserTokens")
public class UserTokensDAO implements UserTokens
{
    @Autowired
    private NamedParameterJdbcTemplate jdbc;
    @Value("${custom.tokens.expire-days}")
    private int expireDays;

    private Timestamp getTimestamp(int addDays)
    {
        Calendar date = DateUtils.createNow();
        date.add(Calendar.DATE, addDays);
        return new Timestamp(date.getTimeInMillis());
    }

    @Override
    public UUID add(UUID userId)
    {
        String SQL_ADD = "insert into user_tokens(token, user_id, expires) values (:t, UNHEX(CONCAT(REPLACE(:uid, '-', ''))), :exp)";
        UUID token = UUID.randomUUID();
        Calendar date = DateUtils.createNow();
        date.add(Calendar.DATE, this.expireDays);
        jdbc.update(SQL_ADD, new MapSqlParameterSource()
                .addValue("t", token)
                .addValue("uid", userId,  Types.VARCHAR)
                .addValue("exp", this.getTimestamp(this.expireDays), Types.TIMESTAMP)
        );
        return token;
    }

    @Override
    public boolean checkExpired(UUID token)
    {
        //TODO зашить дату протухания в токен и проверять сначала дату на входе, только потом проверять токен в бд без условия по дате
        String SQL_CHECK = "select 1 from user_tokens where token = :t and expires < :now";

        return jdbc.queryForObject(
                SQL_CHECK,
                new MapSqlParameterSource()
                        .addValue("t", token,  Types.VARCHAR)
                        .addValue("now", this.getTimestamp(0)),
                int.class
        ) == null;
    }
}
