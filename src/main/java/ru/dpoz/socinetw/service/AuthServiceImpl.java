package ru.dpoz.socinetw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dpoz.socinetw.SocinetwApplication;
import ru.dpoz.socinetw.model.UserEntity;
import ru.dpoz.socinetw.model.UserHobbiesEntity;
import ru.dpoz.socinetw.model.UserSecretEntity;
import ru.dpoz.socinetw.repository.intf.User;
import ru.dpoz.socinetw.repository.intf.UserHobbies;
import ru.dpoz.socinetw.repository.intf.UserSecret;
import ru.dpoz.socinetw.security.UserSecretDetails;
import ru.dpoz.socinetw.service.helpers.SignUpInfo;
import ru.dpoz.socinetw.service.intf.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService
{
    @Autowired
    User userDAO;
    @Autowired
    UserHobbies userHobbiesDAO;
    @Autowired
    UserSecret secretDAO;

    @Override
    public UserSecretDetails getCurrentUser()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserSecretDetails) {
            return ((UserSecretDetails) principal);
        }
        else
            return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserSecretEntity userData = this.secretDAO.findUsername(username);
        if (userData == null)
            throw new UsernameNotFoundException(username);
        return new UserSecretDetails(userData);
    }

    @Override
    public boolean checkUsernameExists(String username)
    {
        return  this.secretDAO.findUsername(username) != null;
    }

    @Override
    @Transactional
    public boolean signUp(SignUpInfo signUpInfo)
    {
        //TODO проверка входящей информации, сервис верификации, try-catch и тд
        UUID userId =  userDAO.add(signUpInfo.getUser());
        signUpInfo.getHobbies().forEach(id -> userHobbiesDAO.add(userId, id));
        signUpInfo.getSecret().setUserId(userId);
        secretDAO.add(signUpInfo.getSecret());
        return true;
    }

    @Override
    @Transactional
    public boolean signUpBulk(List<SignUpInfo> signUpInfoList)
    {
        Long t1 = System.currentTimeMillis();
        SocinetwApplication.logger.info("adding bulk users");
        List<UserEntity> users = new ArrayList<>();
        signUpInfoList.forEach(i -> users.add(i.getUser()));
        List<UUID> userIds = userDAO.addAll(users);
        Long t2 = System.currentTimeMillis();
        SocinetwApplication.logger.info("complete in {}", (t2 - t1)/1000F);

        t1 = System.currentTimeMillis();
        SocinetwApplication.logger.info("perform convert to hobbies list");
        List<UserHobbiesEntity> hobbies = new ArrayList<>();
        List<UserSecretEntity> userSecrets = new ArrayList<>();
        for (int i = 0; i < signUpInfoList.size(); i++) {
            List<Integer> userHobbies = signUpInfoList.get(i).getHobbies();
            for (int j = 0; j < userHobbies.size(); j++) {
                hobbies.add(new UserHobbiesEntity(userIds.get(i), (short)userHobbies.get(j).intValue()));
            }
            signUpInfoList.get(i).getSecret().setUserId(userIds.get(i));
            userSecrets.add(signUpInfoList.get(i).getSecret());
        }
        t2 = System.currentTimeMillis();
        SocinetwApplication.logger.info("complete in {}", (t2 - t1)/1000F);

        t1 = System.currentTimeMillis();
        SocinetwApplication.logger.info("add bulk hobbies");
        userHobbiesDAO.addAll(hobbies);
        t2 = System.currentTimeMillis();
        SocinetwApplication.logger.info("complete in {}", (t2 - t1)/1000F);

        t1 = System.currentTimeMillis();
        SocinetwApplication.logger.info("add bulk secrets");
        secretDAO.addAll(userSecrets);
        t2 = System.currentTimeMillis();
        SocinetwApplication.logger.info("complete in {}", (t2 - t1)/1000F);

        return true;
    }
}
