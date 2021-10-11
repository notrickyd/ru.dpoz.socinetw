package ru.dpoz.socinetw.repository.intf;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.dpoz.socinetw.model.NewsFeedFriends;
import ru.dpoz.socinetw.model.NewsFeedEntity;

import java.util.List;
import java.util.UUID;

public interface NewsFeedRepository extends CrudRepository<NewsFeedEntity, Long>
{
    Iterable<NewsFeedEntity> findAllByUserIdOrderByTimestampxDesc(UUID userId);

    // TODO Переделать все uuid на binary(16)
    @Query(value =  "select nf.message, nf.timestampx, concat(u.first_name, ' ', u.last_name) name " +
                    "from user_friends uf " +
                    "  inner join news_feed nf on nf.user_id = F_UUID_TO_BIN(uf.friend_id) " +
                    "  inner join users u on u.user_id = uf.friend_id " +
                    "where uf.user_id = F_BIN_TO_UUID(:user_id) " +
                    "order by nf.timestampx desc", nativeQuery = true)
    List<NewsFeedFriends> getFriendsNewsFeed(@Param("user_id") UUID userId);
}
