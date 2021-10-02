package ru.dpoz.socinetw.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dpoz.socinetw.model.UserEntity;
import ru.dpoz.socinetw.repository.intf.User;
import ru.dpoz.socinetw.repository.intf.UserFriends;
import ru.dpoz.socinetw.repository.intf.UserHobbies;
import ru.dpoz.socinetw.response.*;
import ru.dpoz.socinetw.security.UserSecretDetails;
import ru.dpoz.socinetw.service.intf.AuthService;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController
{
    @Autowired
    User userDAO;
    @Autowired
    UserFriends userFriendsDAO;
    @Autowired
    UserHobbies userHobbiesDAO;
    @Autowired
    AuthService authService;


    @GetMapping(path = {"/", ""}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponseEntity index()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            HashMap<String, Object> userDetails = new LinkedHashMap<>();
            userDetails.put("user", userDAO.get(user.getUserSecretEntity().getUserId()));
            userDetails.put("hobby", userHobbiesDAO.get(user.getUserSecretEntity().getUserId()));
            return new OkResponseEntity("", userDetails);
        }
        return new NeedsAuthResponseEntity();
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponseEntity add(@RequestBody UserEntity user)
    {
        user.setUserId(this.userDAO.add(user));
        return new BasicResponseEntity(new BasicResponse("", 0, user), HttpStatus.CREATED);
    }

    @GetMapping(path = "/friends", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponseEntity friends()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            List<UserEntity> friends = userFriendsDAO.getUsers(user.getUserSecretEntity().getUserId());
            return new OkResponseEntity("", friends);
        }
        return new NeedsAuthResponseEntity();
    }

    @PostMapping(path = "/friends/add/{userId}")
    public BasicResponseEntity addFriend(@PathVariable UUID userId)
    {
        UserSecretDetails currentUser = this.authService.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getUserSecretEntity().getUserId().equals(userId))
                return new ConflictResponseEntity("Похоже на раздвоение личности, друг :).", null);
            UserEntity friend = userDAO.get(userId);
            if (friend == null)
                return new NotFoundResponseEntity("Пользователь не найден.", null);
            if (!userFriendsDAO.add(currentUser.getUserSecretEntity().getUserId(), friend.getUserId()))
                return new ConflictResponseEntity("Пользователь уже твой друг.", null);
            else
                return new OkResponseEntity(String.format("Ты зафрендил %s %s!", friend.getFirstName(), friend.getLastName()), null);
        }
        return new NeedsAuthResponseEntity();
    }
}
