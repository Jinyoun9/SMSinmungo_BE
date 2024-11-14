package com.smsinmungo.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token != null && isValidToken(token)) { // 토큰 검증 로직
            // 유효한 토큰 처리
            return message;
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private boolean isValidToken(String token) {
        // 토큰 유효성 검증 로직 (예: JWT 라이브러리 사용)
        return true;
    }
}
