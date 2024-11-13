package com.smsinmungo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smsinmungo.domain.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public MeetingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    public void saveMessage(String roomId, String sender, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .build();
        redisTemplate.opsForList().rightPush("chatroom:" + roomId.toLowerCase(), chatMessage);

        printChatHistory(roomId);
    }
    public void printChatHistory(String roomId) {
        List<Object> chatHistory = redisTemplate.opsForList().range("chatroom:" + roomId, 0, -1);

        if (chatHistory != null && !chatHistory.isEmpty()) {
            System.out.println("Room ID: " + roomId + " Chat History:");
            for (Object message : chatHistory) {
                System.out.println(message.toString());
            }
        } else {
            System.out.println("No chat history found for Room ID: " + roomId);
        }
    }
}
