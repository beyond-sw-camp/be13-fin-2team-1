package com.gandalp.gandalp.config.stomp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// 인증 작업을 해주는 StompHanlder : 토큰을 꺼내서 우리가 만들어준 토큰이 맞는지 검증해준다.
@Component
public class StompHandler implements ChannelInterceptor {

    @Value("${springboot.jwt.secret}")
    private String secretKey;



}
