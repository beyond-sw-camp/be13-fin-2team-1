package com.gandalp.gandalp.config.websocket;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

// 인증 작업: 토큰을 꺼내서 우리가 만든 토큰이 맞는지 검증한다.
@Component
public class StompHandler implements ChannelInterceptor {


	@Value("${springboot.jwt.secret}")
	private String secret;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		// accessor 안에서 토큰을 꺼낼 수 있다.
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


		if (StompCommand.CONNECT == accessor.getCommand()){

			try{
				System.out.println("connect 요청시 토큰 유효성 검증 ");

				String bearerToken = accessor.getFirstNativeHeader("Authorization");
				String token = bearerToken.substring(7);


				// 토큰 검증
				Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token);
				System.out.println("토큰 검증 완료");

			}catch (Exception e){
				throw new IllegalArgumentException("Invalid STOMP token", e);
			}


		}
		return message;


	}
}
