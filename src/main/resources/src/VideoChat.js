import React, { useState, useEffect, useRef } from "react";
import io from "socket.io-client";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "./style.css";

const VideoChat = () => {
  const [roomName, setRoomName] = useState("");
  const [inRoom, setInRoom] = useState(false);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [localStream, setLocalStream] = useState(null);
  const [remoteStream, setRemoteStream] = useState(null);
  const [isStreamOwner, setIsStreamOwner] = useState(false); // 스트림 송출 권한 상태
  const [isStreamActive, setIsStreamActive] = useState(false);
  const [currentNum, setCurrentNum] = useState("");
  const localVideoRef = useRef(null);
  const remoteVideoRef = useRef(null);
  const socketRef = useRef(null);
  const peerConnectionRef = useRef(null);

  useEffect(() => {
    // Socket.IO 서버 연결
    socketRef.current = io.connect("http://localhost:8000");

    // 채팅 메시지 수신 리스너
    socketRef.current.on("chat message", (data) => {
      setMessages((prevMessages) => [...prevMessages, `${data.username}: ${data.message}`]);
    });

    // 스트림 활성 상태 수신
    socketRef.current.on("streamActive", (isActive) => {
      setIsStreamActive(isActive);
    });

    // 현재 인원 정보 수신
    socketRef.current.on("currentNum", (num) => {
      setCurrentNum(num);
    });

    return () => {
      socketRef.current.disconnect();
    };
  }, []);

  // 방 생성 및 스트림 시작 함수 (방 만든 사람에게만 권한 부여)
  const createRoom = async () => {
    if (roomName === "") {
      alert("Room name cannot be empty!");
      return;
    }

    socketRef.current.emit("createRoom", roomName);
    setInRoom(true);

    // 방 만든 사람에게만 스트림 권한 부여
    setIsStreamOwner(true);

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
      setLocalStream(stream);
      localVideoRef.current.srcObject = stream;

      initializePeerConnection(stream);

      socketRef.current.emit("streamActive", true, roomName);
      setIsStreamActive(true);

      const offer = await peerConnectionRef.current.createOffer();
      await peerConnectionRef.current.setLocalDescription(offer);
      socketRef.current.emit("offer", { room: roomName, sdp: offer });
    } catch (error) {
      console.error("Error accessing media devices:", error);
      alert("Unable to access camera and microphone. Please check if another application is using them.");
    }
  };

  // 방에 참여하여 스트림 수신 함수 (입장한 사람은 시청 모드)
  const joinRoom = async () => {
    if (roomName === "") {
      alert("Room name cannot be empty!");
      return;
    }

    socketRef.current.emit("joinRoom", roomName);
    setInRoom(true);

    // 시청 모드로 입장하므로 스트림 송출 권한은 설정하지 않음

    // 이미 스트림이 활성화된 경우 송출 화면 요청
    if (isStreamActive) {
      socketRef.current.emit("requestStream", roomName);
    }
  };

  const initializePeerConnection = (stream) => {
    if (peerConnectionRef.current) return;

    peerConnectionRef.current = new RTCPeerConnection({
      iceServers: [{ urls: "stun:stun.l.google.com:19302" }],
    });

    stream.getTracks().forEach((track) => peerConnectionRef.current.addTrack(track, stream));

    peerConnectionRef.current.onicecandidate = (event) => {
      if (event.candidate) {
        socketRef.current.emit("candidate", { room: roomName, candidate: event.candidate });
      }
    };

    peerConnectionRef.current.ontrack = (event) => {
      setRemoteStream(event.streams[0]);
      remoteVideoRef.current.srcObject = event.streams[0];
    };
  };

  const handleReceiveOffer = async ({ sdp }) => {
    if (!peerConnectionRef.current) initializePeerConnection();

    await peerConnectionRef.current.setRemoteDescription(new RTCSessionDescription(sdp));
    const answer = await peerConnectionRef.current.createAnswer();
    await peerConnectionRef.current.setLocalDescription(answer);
    socketRef.current.emit("answer", { room: roomName, sdp: answer });
  };

  const handleReceiveAnswer = async ({ sdp }) => {
    await peerConnectionRef.current.setRemoteDescription(new RTCSessionDescription(sdp));
  };

  const handleNewICECandidateMsg = ({ candidate }) => {
    const iceCandidate = new RTCIceCandidate(candidate);
    peerConnectionRef.current.addIceCandidate(iceCandidate);
  };

  const handleSendMessage = () => {
    if (message.trim() === "") return;
    const payload = {
      room: roomName,
      username: "박진영",
      message: message,
    };
    socketRef.current.emit("chat message", payload);
    setMessage("");
  };

  return (
    <div className="container">
      <h1 className="text-center my-3">현재 방 번호: {roomName}</h1>
      {!inRoom && (
        <div className="d-flex justify-content-center mb-3">
          <div className="input-group input-group-lg" style={{ maxWidth: "400px" }}>
            <input
              type="text"
              className="form-control"
              placeholder="Enter room"
              value={roomName}
              onChange={(e) => setRoomName(e.target.value)}
            />
            <button className="btn btn-primary" onClick={createRoom}>
              방 만들기
            </button>
            <button className="btn btn-secondary" onClick={joinRoom}>
              방 입장
            </button>
          </div>
        </div>
      )}

      {inRoom && (
        <div className="d-flex flex-column align-items-center mt-3">
          <div
            id="remoteVideoContainer"
            className="bg-dark d-flex align-items-center justify-content-center"
            style={{ width: "600px", height: "450px" }}
          >
            <video muted ref={localVideoRef} autoPlay style={{ width: "600px", height: "450px" }} />
            <video ref={remoteVideoRef} autoPlay style={{ width: "600px", height: "450px" }} />
          </div>
          <h3>현재 인원: {currentNum}</h3>

          {/* 채팅 메시지 */}
          <ul id="messages" className="list-group mb-3" style={{ width: "600px", maxHeight: "200px", overflowY: "auto" }}>
            {messages.map((msg, index) => (
              <li key={index} className="list-group-item">{msg}</li>
            ))}
          </ul>

          <div className="input-group input-group-lg" style={{ maxWidth: "600px" }}>
            <input
              type="text"
              className="form-control"
              placeholder="Enter message"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
            />
            <button className="btn btn-primary" onClick={handleSendMessage}>
              Send
            </button>
          </div>
        </div>
      )}

      {/* 스트림 소유자만 카메라 켜기 버튼 활성화 */}
      {isStreamOwner && inRoom && (
        <button className="btn btn-primary mt-3" onClick={createRoom}>
          카메라 켜기
        </button>
      )}
    </div>
  );
};

export default VideoChat;
