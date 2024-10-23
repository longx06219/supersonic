package com.tencent.supersonic.auth.api.authentication.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SsoReq {

    @NotBlank(message = "ssoToken can not be null")
    private String ssoToken;

    @NotBlank(message = "sysCode can not be null")
    private String sysCode = "factory";

}
