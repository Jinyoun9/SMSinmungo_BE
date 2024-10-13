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
        } else if (connectedClients > 1) {
            client.sendEvent("joined", room);
            users.put(client.getSessionId().toString(), room);
            client.sendEvent("setCaller", rooms.get(room));
        } else {
            client.sendEvent("full", room);
        }

        // 방에 있는 모든 클라이언트에게 현재 인원 수 전송
        server.getRoomOperations(room).sendEvent("currentNum", connectedClients);
        printLog("onJoinRoom", client, room);
    }

    @OnEvent("ready")
    public void onReady(SocketIOClient client, String room, AckRequest ackRequest) {
        client.getNamespace().getBroadcastOperations().sendEvent("ready", room);
        printLog("onReady", client, room);
    }

    @OnEvent("candidate")
    public void onCandidate(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        client.getNamespace().getRoomOperations(room).sendEvent("candidate", payload);
        printLog("onCandidate", client, room);
    }

    @OnEvent("offer")
    public void onOffer(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        Object sdp = payload.get("sdp");
        client.getNamespace().getRoomOperations(room).sendEvent("offer", sdp);
        printLog("onOffer", client, room);
    }

    @OnEvent("answer")
    public void onAnswer(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        Object sdp = payload.get("sdp");
        client.getNamespace().getRoomOperations(room).sendEvent("answer", sdp);
        printLog("onAnswer", client, room);
    }

    @OnEvent("leaveRoom")
    public void onLeaveRoom(SocketIOClient client, String room) {
        client.leaveRoom(room);
        printLog("onLeaveRoom", client, room);
    }
    @OnEvent("chat message")
    public void onChatMessage(SocketIOClient client, Map<String, Object> payload) {
        String room = (String) payload.get("room");
        String sender = (String) payload.get("username");
        String message = (String) payload.get("message");
        client.getNamespace().getRoomOperations(room).sendEvent("chat message", payload);
        meetingService.saveMessage(room, sender, message);
        printLog("onChatMessage", client, room);
    }

    @OnEvent("handsup")
    public void onHandsUp(SocketIOClient client, Map<String, Object> payload){
        String userName = (String) payload.get("username");
        System.out.println(userName);
        handRaiseList.add(userName);
        client.getNamespace().getBroadcastOperations().sendEvent("updateHandRaiseList", handRaiseList);
    }
    @OnEvent("handsdown")
    public void onHandsDown(SocketIOClient client, Map<String, Object> payload){
        String userName = (String) payload.get("username");
        handRaiseList.remove(userName);
        client.getNamespace().getBroadcastOperations().sendEvent("updateHandRaiseList", handRaiseList);
    }
    @OnEvent("currentNum")
    public void onCurrentNum(SocketIOClient client, Map<String, Object> payload){
        String room = (String) payload.get("room");
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
