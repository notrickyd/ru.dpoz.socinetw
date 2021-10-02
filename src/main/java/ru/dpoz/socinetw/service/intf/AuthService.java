package ru.dpoz.socinetw.service.intf;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dpoz.socinetw.security.UserSecretDetails;
import ru.dpoz.socinetw.service.helpers.SignUpInfo;

import java.util.List;

public interface AuthService extends UserDetailsService
{
    boolean signUp(SignUpInfo signUpInfo);
    boolean signUpBulk(List<SignUpInfo> signUpInfoList);

    boolean checkUsernameExists(String username);

    UserSecretDetails getCurrentUser();
}
