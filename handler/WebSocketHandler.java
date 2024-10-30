package com.smsinmungo.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsinmungo.service.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final SocketIOServer server;
    private final MeetingService meetingService;
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> rooms = new HashMap<>();
    private static final Map<String, Boolean> roomStreamStatus = new HashMap<>(); // 각 방의 스트림 상태
    private List<String> handRaiseList = new ArrayList<>();

    public WebSocketHandler(SocketIOServer server, MeetingService meetingService) {
        this.server = server;
        this.meetingService = meetingService;
        server.addListeners(this);
        server.start();
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("Client connected: " + client.getSessionId());
        String clientId = client.getSessionId().toString();
        users.put(clientId, null);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String clientId = client.getSessionId().toString();
        String room = users.get(clientId);
        if (!Objects.isNull(room)) {
            System.out.println(String.format("Client disconnected: %s from : %s", clientId, room));
            users.remove(clientId);
            client.getNamespace().getRoomOperations(room).sendEvent("userDisconnected", clientId);

            // 방에서 스트림을 송출하던 사용자가 나가면 상태 초기화
            if (rooms.get(room).equals(clientId)) {
                roomStreamStatus.put(room, false); // 스트림 비활성화
                client.getNamespace().getRoomOperations(room).sendEvent("streamActive", false);
            }
        }
        printLog("onDisconnect", client, room);
    }

    @OnEvent("joinRoom")
    public void onJoinRoom(SocketIOClient client, String room) {
        client.joinRoom(room);
        int connectedClients = server.getRoomOperations(room).getClients().size();

        if (connectedClients == 1) {
            client.sendEvent("created", room);
            users.put(client.getSessionId().toString(), room);
            rooms.put(room, client.getSessionId().toString());
            roomStreamStatus.put(room, true); // 첫 사용자는 스트림 활성화
            client.getNamespace().getRoomOperations(room).sendEvent("streamActive", true); // 첫 사용자에게 스트림 활성화 알림
        } else {
            client.sendEvent("joined", room);
            users.put(client.getSessionId().toString(), room);
            client.sendEvent("setCaller", rooms.get(room));
            client.sendEvent("streamActive", roomStreamStatus.get(room)); // 나머지 사용자들에게 현재 스트림 상태 알림
        }

        server.getRoomOperations(room).sendEvent("currentNum", connectedClients);
        printLog("onJoinRoom", client, room);
    }

    // 나머지 메서드 그대로 유지...

    @OnEvent("handsup")
    public void onHandsUp(SocketIOClient client, Map<String, Object> payload) {
        String userName = (String) payload.get("username");
        System.out.println(userName);
        handRaiseList.add(userName);
        client.getNamespace().getBroadcastOperations().sendEvent("updateHandRaiseList", handRaiseList);
    }

    @OnEvent("handsdown")
    public void onHandsDown(SocketIOClient client, Map<String, Object> payload) {
        String userName = (String) payload.get("username");
        handRaiseList.remove(userName);
        client.getNamespace().getBroadcastOperations().sendEvent("updateHandRaiseList", handRaiseList);
    }

    private static void printLog(String header, SocketIOClient client, String room) {
        if (room == null) return;
        int size = 0;
        try {
            size = client.getNamespace().getRoomOperations(room).getClients().size();
        } catch (Exception e) {
            log.error("error ", e);
        }
        log.info("#ConncetedClients - {} => room: {}, count: {}", header, room, size);
    }
}
