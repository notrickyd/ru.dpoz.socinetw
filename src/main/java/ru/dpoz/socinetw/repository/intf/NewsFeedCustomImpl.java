package ru.dpoz.socinetw.repository.intf;

import org.apache.commons.lang3.StringUtils;
import ru.dpoz.socinetw.cache.NewsFeedCacheItem;
import ru.dpoz.socinetw.model.NewsFeedFriends;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

public class NewsFeedCustomImpl implements NewsFeedCustom
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<NewsFeedFriends> getFriendsNewsFeed(UUID userId)
    {
        String SQL =
                "select nf.message as message, nf.timestampx as timestampx, concat(u.first_name, ' ', u.last_name) as name, "
                + "LOWER(CONCAT("
                + "   LEFT(HEX(uf.friend_id), 8), '-', "
                + "   MID(HEX(uf.friend_id), 9, 4), '-', "
                + "   MID(HEX(uf.friend_id), 13, 4), '-', "
                + "   MID(HEX(uf.friend_id), 17, 4), '-', "
                + "   RIGHT(HEX(uf.friend_id), 12))) as user_id, "
                + "nf.id as id "
                + "from user_friends uf "
                + "  inner join news_feed nf on nf.user_id = uf.friend_id "
                + "  inner join users u on u.user_id = uf.friend_id "
                + "where uf.user_id = UNHEX(CONCAT(REPLACE(:uid, '-', ''))) "
                + "order by nf.timestampx desc";
        Query query = em.createNativeQuery(SQL, "NewsFeedCustomImpl_FriendsQuery");
        query.setParameter("uid", userId.toString());
        return (List<NewsFeedFriends>) query.getResultList();
    }

    @Override
    public List<NewsFeedFriends> getNewsFeed(UUID userId, List<NewsFeedCacheItem> feedIdList)
    {
        String SQL_CTE_PART = " SELECT {0} UNION";
        StringBuilder cteTable = new StringBuilder();
        int i = 0;
        for (i = 0; i < feedIdList.size(); i++) {
            if (i == 0)
                cteTable.append(MessageFormat.format(SQL_CTE_PART, feedIdList.get(i).getNewsFeedId() + " as id "));
            else
                cteTable.append(MessageFormat.format(SQL_CTE_PART, feedIdList.get(i).getNewsFeedId()));
        }
        cteTable = new StringBuilder(StringUtils.substringBeforeLast(cteTable.toString(), "UNION"));

        String SQL =
                "select nf.message as message, nf.timestampx as timestampx, concat(u.first_name, ' ', u.last_name) as name, " +
                "   LOWER(CONCAT(" +
                "       LEFT(HEX(nf.user_id), 8), '-', " +
                "       MID(HEX(nf.user_id), 9, 4), '-', " +
                "       MID(HEX(nf.user_id), 13, 4), '-', " +
                "       MID(HEX(nf.user_id), 17, 4), '-', " +
                "       RIGHT(HEX(nf.user_id), 12))) as user_id, " +
                "   nf.id as id " +
                "from (" + cteTable + ") as cte " +
                "   inner join news_feed nf on nf.id = cte.id " +
                "   inner join users u on u.user_id = nf.user_id ";
        Query query = em.createNativeQuery(SQL, "NewsFeedCustomImpl_FriendsQuery");
        return (List<NewsFeedFriends>) query.getResultList();
    }
}


