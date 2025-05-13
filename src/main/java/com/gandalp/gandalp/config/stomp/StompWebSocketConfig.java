package com.gandalp.gandalp.config.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:5174")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        // /publish/1 형태로 메시지를 발행해야 함을 설정
        // /publish 로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMappin 메소드로 라우팅 된다.

        registry.setApplicationDestinationPrefixes("/app");


        // /topic/1 형태로 메시지를 수신해야 함
        registry.enableSimpleBroker("/topic");
    }



}
