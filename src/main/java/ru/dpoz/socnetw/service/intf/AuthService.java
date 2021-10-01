package ru.dpoz.socnetw.service.intf;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dpoz.socnetw.security.UserSecretDetails;
import ru.dpoz.socnetw.service.helpers.SignUpInfo;

import java.util.List;

public interface AuthService extends UserDetailsService
{
    boolean signUp(SignUpInfo signUpInfo);
    boolean signUpBulk(List<SignUpInfo> signUpInfoList);

    boolean checkUsernameExists(String username);

    UserSecretDetails getCurrentUser();
}
