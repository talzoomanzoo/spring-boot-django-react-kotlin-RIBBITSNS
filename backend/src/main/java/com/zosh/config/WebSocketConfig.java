package com.zosh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// TODO Auto-generated method stub
		registry.addEndpoint("/chat/{userId}")
			.setAllowedOrigins("http://localhost:3000/") //모든 주소를 허용한다/
			.withSockJS();
	}//웹소켓 연결 주소

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// TODO Auto-generated method stub
		registry.enableSimpleBroker("/chat");
		registry.setApplicationDestinationPrefixes("/app");//메시지를 전송할 때 사용하는 url
	}//메시지 전송
	
}
