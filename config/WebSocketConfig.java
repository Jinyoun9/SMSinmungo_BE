package com.smsinmungo.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.smsinmungo.handler.ChatWebSocketHandler;
import com.smsinmungo.interceptor.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    public SocketIOServer socketIOServer() throws Exception{
        com.corundumstudio.socketio.Configuration configuration
                = new com.corundumstudio.socketio.Configuration();

        configuration.setHostname("localhost");
        configuration.setPort(8000);

        return new SocketIOServer(configuration);
    }
}
