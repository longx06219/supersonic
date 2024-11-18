package com.tencent.supersonic.headless.server.facade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.common.pojo.User;
import com.tencent.supersonic.headless.api.pojo.request.QueryMapReq;
import com.tencent.supersonic.headless.server.facade.service.ChatLayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semantic/meta")
@Slf4j
public class MetaDiscoveryApiController {

    @Autowired
    private ChatLayerService chatLayerService;

    @PostMapping("map")
    public Object map(@RequestBody QueryMapReq queryMapReq, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User user = UserHolder.findUser(request, response);
        queryMapReq.setUser(user);
        return chatLayerService.map(queryMapReq);
    }
}
