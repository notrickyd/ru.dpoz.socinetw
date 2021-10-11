package ru.dpoz.socinetw.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dpoz.socinetw.model.NewsFeedFriends;
import ru.dpoz.socinetw.model.NewsFeedEntity;
import ru.dpoz.socinetw.repository.intf.NewsFeedRepository;
import ru.dpoz.socinetw.response.BasicResponseEntity;
import ru.dpoz.socinetw.response.NeedsAuthResponseEntity;
import ru.dpoz.socinetw.response.OkResponseEntity;
import ru.dpoz.socinetw.response.RedirectResponseEntity;
import ru.dpoz.socinetw.security.UserSecretDetails;
import ru.dpoz.socinetw.service.intf.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feed")
public class NewsFeedController
{
    @Autowired
    private NewsFeedRepository newsFeed;

    @Autowired
    AuthService authService;

    @PostMapping("/add")
    public BasicResponseEntity add(@RequestBody NewsFeedEntity message)
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            message.setUserId(user.getUserSecretEntity().getUserId());
            newsFeed.save(message);
            return new RedirectResponseEntity("/user");
        }
        return new NeedsAuthResponseEntity();
    }

    @GetMapping("/self")
    public BasicResponseEntity self()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            Iterable<NewsFeedEntity> news = newsFeed.findAllByUserIdOrderByTimestampxDesc(user.getUserSecretEntity().getUserId());
            return new OkResponseEntity("",null);
        }
        return new NeedsAuthResponseEntity();
    }

    @GetMapping("/friends")
    public BasicResponseEntity friends()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            UUID userId = user.getUserSecretEntity().getUserId();
            Iterable<NewsFeedFriends> friendNews = newsFeed.getFriendsNewsFeed(userId);
            return new OkResponseEntity("", friendNews);
        }
        return new NeedsAuthResponseEntity();
    }

}
