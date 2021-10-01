package ru.dpoz.socnetw.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dpoz.socnetw.response.BasicResponseEntity;
import ru.dpoz.socnetw.response.LoginExistsResponseEntity;
import ru.dpoz.socnetw.response.OkResponseEntity;
import ru.dpoz.socnetw.service.helpers.SignUpInfo;
import ru.dpoz.socnetw.service.intf.AuthService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController
{
    @Autowired
    private AuthService authService;

    @PostMapping(path = "/signup")
    public BasicResponseEntity signup (
            @RequestBody SignUpInfo signUpInfo
    )
    {
        if (authService.checkUsernameExists(signUpInfo.getSecret().getUsername()) )
            return new LoginExistsResponseEntity(signUpInfo.getSecret().getUsername());
        else
            authService.signUp(signUpInfo);
        HashMap<String, String> data = new LinkedHashMap<>();
        data.put("url", "/signin");
        return new OkResponseEntity("", data);
    }

    @PostMapping(path = "/signupbulk")
    public BasicResponseEntity signupbulk (
            @RequestBody List<SignUpInfo> signUpInfoList
    )
    {
       /* for (SignUpInfo si: signUpInfoList) {
            if (authService.checkUsernameExists(si.getSecret().getUsername()) )
                return new LoginExistsResponseEntity(si.getSecret().getUsername());
        }*/
        authService.signUpBulk(signUpInfoList);
        HashMap<String, String> data = new LinkedHashMap<>();
        data.put("ok", "ok");
        return new OkResponseEntity("", data);
    }

}
