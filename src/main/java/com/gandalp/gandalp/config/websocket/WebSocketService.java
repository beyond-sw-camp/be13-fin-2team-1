package com.gandalp.gandalp.config.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {
	// 메시지 전송 서비스: 클라이언트에게 메시지를 보내는 역할을 한다.
	// configureMessageBroker 에서 메시지 브로커의 경로를 "/topic" 으로 설정하였기 때문에,
	//  "/topic"을 기반으로 분기를 만들어준다.

	// destination에 "/topic/finish" 로 만들면, "/topic/finish" 로 모든 메시지를 브로드캐스트한다.

	private final SimpMessagingTemplate messagingTemplate;

	public void sendMessage(String destination, String message){

		messagingTemplate.convertAndSend(destination, message);
	}
}
