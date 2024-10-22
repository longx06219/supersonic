package com.tencent.supersonic.authentication;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.authentication.utils.UserTokenUtils;
import com.tencent.supersonic.headless.BaseTest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTokenUtilsTest extends BaseTest {

    @Autowired
    UserTokenUtils userTokenUtils;

    @Test
    public void getUser() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9pc19hZG1pbiI6MSwidG9rZW5fdXNlcl9pZCI6MSwidG9rZW5fdXNlcl9uYW1lIjoiYWRtaW4iLCJ0b2tlbl91c2VyX2Rpc3BsYXlfbmFtZSI6ImFkbWluIiwidG9rZW5fY3JlYXRlX3RpbWUiOjE3Mjk1Nzg0OTIzNTQsInRva2VuX3VzZXJfcGFzc3dvcmQiOiJjM1Z3WlhKemIyNXBZMEJpYVdOdmJka3RKSllXdzZBM3JFbUJVUHpibi82RE5lWW5EK3kzbUF3REtFTVMzS1ZUIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3Mjk2NTA0OTJ9.KSSnxEXWg2NnAfEactRoxbcfe7VvIquS5H4p-WGLBGptCA8VP_kE-pYtjNsrQfcD-Js0k1PZJBux2Ve4vp5eFA";
        String appKey = "supersonic";
        User user = userTokenUtils.getUser(token, appKey);
        System.out.println(user);
    }

    @Test
    public void getClaims() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9pc19hZG1pbiI6MSwidG9rZW5fdXNlcl9pZCI6MSwidG9rZW5fdXNlcl9uYW1lIjoiYWRtaW4iLCJ0b2tlbl91c2VyX2Rpc3BsYXlfbmFtZSI6ImFkbWluIiwidG9rZW5fY3JlYXRlX3RpbWUiOjE3Mjk1Nzg0OTIzNTQsInRva2VuX3VzZXJfcGFzc3dvcmQiOiJjM1Z3WlhKemIyNXBZMEJpYVdOdmJka3RKSllXdzZBM3JFbUJVUHpibi82RE5lWW5EK3kzbUF3REtFTVMzS1ZUIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3Mjk2NTA0OTJ9.KSSnxEXWg2NnAfEactRoxbcfe7VvIquS5H4p-WGLBGptCA8VP_kE-pYtjNsrQfcD-Js0k1PZJBux2Ve4vp5eFA";
        String appKey = "supersonic";
        Claims claims = userTokenUtils.getClaims(token, appKey).get();
        System.out.println(claims);
    }

    @Test
    public void getClaims1() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ4aW5zb2Z0Iiwic3ViIjoiand0VG9rZW4iLCJhdWQiOiJhbGwiLCJpYXQiOjE3Mjk1ODg5NTUsImV4cCI6MTczMjE4MDk1NSwiZGF0YSI6IntcInVzZXJJZFwiOlwiNTM0MDc2Mzk2NTc1NTM5MlwiLFwidXNlckNvZGVcIjpcIlVDMDAwMDAxXCIsXCJ1c2VyUmVhbG5hbWVcIjpcIuadqOathua4ilwiLFwiYnVzaW5lc3NJZFwiOlwiNTM0MDc2MzkwMjg0MDgzMlwiLFwib3JnYW5pemF0aW9uSWRcIjpcIjcxNDU4MDYwNTQ0Mjg2NzJcIixcImxvZ2luQWxpYXNcIjpcInl4eTFcIixcImNsaWVudERldmljZVR5cGVcIjpcInBjXCJ9IiwianRpIjoiNTIzNmM2ZjIyMGYwNGJlNzhjOTczZWY0NjI4NGZmYTgifQ.PWRuEmm-DyRqJc2V5Ugza80Ip9HjT9-qNSOztpJet6k";
        String appKey = "factory";
        Claims claims = userTokenUtils.getClaims(token, appKey).get();
        System.out.println(claims);
    }

}
