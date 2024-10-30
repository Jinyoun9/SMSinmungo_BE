package com.smsinmungo.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.smsinmungo.handler.WebSocketAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Bean
    public SocketIOServer socketIOServer() throws Exception{
        com.corundumstudio.socketio.Configuration configuration
                = new com.corundumstudio.socketio.Configuration();

        configuration.setHostname("localhost");
        configuration.setPort(8000);

        return new SocketIOServer(configuration);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
    }
}
