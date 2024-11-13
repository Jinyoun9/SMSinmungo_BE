package com.smsinmungo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessage {
    private String roomId;
    private String sender;
    private String message;
}
