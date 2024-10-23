package com.tencent.supersonic.auth.authentication.rest;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authentication.pojo.UserWithPassword;
import com.tencent.supersonic.auth.api.authentication.request.SsoReq;
import com.tencent.supersonic.auth.api.authentication.request.UserReq;
import com.tencent.supersonic.auth.api.authentication.service.UserService;
import com.tencent.supersonic.auth.authentication.utils.UserTokenUtils;
import com.tencent.supersonic.common.pojo.exception.AccessException;
import com.tencent.supersonic.common.util.JsonUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class SsoController {

    @Autowired
    UserTokenUtils userTokenUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/sso")
    public String loginByJWT(@RequestBody SsoReq ssoCmd, HttpServletRequest request) {
        String token = null;

        String ssoToken = ssoCmd.getSsoToken();
        String sysCode = ssoCmd.getSysCode();

        // JWT校验合法性（带盐）
        Optional<Claims> claimsOptional = userTokenUtils.getClaims(ssoToken, sysCode);
        if (!claimsOptional.isPresent()) {
            throw new AccessException("authentication failed, please check your token.");
        }

        // JWT解码获取用户信息
        Claims claims = claimsOptional.get();
        String dataString = String.valueOf(claims.get("data"));
        Map<String, Object> data = JsonUtil.toObject(dataString, Map.class);

        String loginAlias = String.valueOf(data.get("loginAlias"));
        String userRealname = String.valueOf(data.get("userRealname"));
        userRealname = ObjectUtils.isEmpty(userRealname) ? loginAlias : userRealname;

        List<User> users = userService.getUserList();
        List<User> list = users.stream().filter(e -> loginAlias.equals(e.getName())).collect(Collectors.toList());

        // 不存在：注册用户，登录生成token；存在：生成token；
        if (ObjectUtils.isEmpty(list)) {
            String name = loginAlias;
            String displayName = userRealname;
            String password = "Q8TwLHvwhhAQ3Fc4mnmQTA==";// Xinsoft@1

            UserReq userCmd = new UserReq();
            userCmd.setName(name);
            userCmd.setPassword(password);
            userService.register(userCmd);

            token = userService.login(userCmd, request);
        } else {
            UserWithPassword user = new UserWithPassword("");
            BeanUtils.copyProperties(list.get(0), user);
            token = userTokenUtils.generateToken(user, request);
        }

        return token;
    }

}
