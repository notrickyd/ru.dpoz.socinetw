package ru.dpoz.socnetw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.DateUtils;
import ru.dpoz.socnetw.SocnetwApplication;
import ru.dpoz.socnetw.model.HobbyEntity;
import ru.dpoz.socnetw.model.UserEntity;
import ru.dpoz.socnetw.repository.intf.Hobby;
import ru.dpoz.socnetw.repository.intf.User;
import ru.dpoz.socnetw.repository.intf.UserFriends;
import ru.dpoz.socnetw.repository.intf.UserHobbies;
import ru.dpoz.socnetw.security.UserSecretDetails;
import ru.dpoz.socnetw.service.intf.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class PagesController
{
    @Autowired
    private Hobby hobbyList;
    @Autowired
    private AuthService authService;
    @Autowired
    private User userDAO;
    @Autowired
    private UserHobbies userHobbiesDAO;
    @Autowired
    private UserFriends userFriendsDAO;

    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("time", DateUtils.createNow().getTime());
        UserSecretDetails currentUser = authService.getCurrentUser();
        boolean loggedIn = currentUser != null;
        model.addAttribute("loggedin", loggedIn);
        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model)
    {
        model.addAttribute("hobbyList", this.hobbyList.getAll());
        UserSecretDetails currentUser = authService.getCurrentUser();
       /* if (currentUser != null)
            return "redirect:/user";*/
        return "signup/index";
    }

    @GetMapping({"/signin", "/signin?error"})
    public String signin()
    {
        UserSecretDetails currentUser = authService.getCurrentUser();
       /* if (currentUser != null)
            return "redirect:/user";*/
        return "signin/index";
    }

    /*@GetMapping({"/signout"})
    public String signout()
    {
        return "redirect:/";
    }*/

    @GetMapping({"/user" ,"/user/{userId}"})
    public String userPage(@PathVariable(required = false) UUID userId, Model model)
    {
        String username = "";
        UserEntity user = new UserEntity();
        List<UserEntity> friends = new ArrayList<>();
        List<HobbyEntity> hobbies = new ArrayList<>();
        UserSecretDetails currentUser = authService.getCurrentUser();
        boolean willViewSelf = userId == null;
        // Просмотр личного профиля, иначе - профиль другого пользователя
        if (willViewSelf){
            if (currentUser != null) {
                userId = currentUser.getUserSecretEntity().getUserId();
                username = currentUser.getUsername();
            }
        }
        else {
            username = ""; // не показываем чужой логин
        }

        if (userId != null) {
            user = this.userDAO.get(userId);
            hobbies = this.userHobbiesDAO.get(userId);
            friends = this.userFriendsDAO.getUsers(userId);
        }
        model.addAttribute("username", username);
        model.addAttribute("user", user);
        model.addAttribute("hobbies", hobbies);
        model.addAttribute("friends", friends);
        return "user/index";
    }

    @GetMapping("/users")
    public String users(@RequestParam(required = false, name = "p") Integer page, Model model)
    {
        if (page == null)
            page = 1;
        Long t1 = System.currentTimeMillis();
        Integer countPages = this.userDAO.count() / 100;
        List<String> pages = new ArrayList<>();
        Integer i = 0;
        if (page <= 3)
            pages.addAll(List.of("1","2","3","4","...",countPages.toString()));
        else
            if (page > 3 && (countPages - page > 2))
                pages.addAll(List.of(
                        "1",
                        "...",
                        Integer.toString(page - 1),
                        page.toString(),
                        Integer.toString(page + 1),
                        "...",
                        countPages.toString()));
            else
            if (countPages - page <= 2)
                pages.addAll(List.of(
                        "1",
                        "...",
                        Integer.toString(countPages - 3),
                        Integer.toString(countPages - 2),
                        Integer.toString(countPages - 1),
                        countPages.toString()));
        List<UserEntity> userList = this.userDAO.getAll(page);
        Long t2 = System.currentTimeMillis();
        SocnetwApplication.logger.info("users fetch of page '{}' completed in {}", page, (t2 - t1)/1000F);
        model.addAttribute("users", userList);
        model.addAttribute("pages", pages);
        return "users/index";
    }

    @GetMapping("/users/search")
    public String usersSearch(Model model)
    {
        return "users/search/index";
    }

    @GetMapping (path="/users/search/{name}", produces = MediaType.TEXT_HTML_VALUE)
    public String usersSearch(@PathVariable() String name, Model model)
    {
        Long t1 = System.currentTimeMillis();
        List<UserEntity> users = userDAO.findByName(name.split(" ")[0], name.split(" ")[1]);
        Long t2 = System.currentTimeMillis();
        SocnetwApplication.logger.info("search of '{}' completed in {}", name, (t2 - t1)/1000F);
        model.addAttribute("users", users);
        return "users/search/usersSearchResult :: usersSearchResult";
    }


    @GetMapping (path="/users/lenta", produces = MediaType.TEXT_HTML_VALUE)
    public String lenta()
    {
        return "";
    }

    @GetMapping("/signupbulk")
    public String random(Model model)
    {
        return "signupbulk/index";
    }


}
