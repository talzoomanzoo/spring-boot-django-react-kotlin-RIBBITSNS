import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { useSelector } from "react-redux";

const customStyles = {
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
  },
};

const Chat = () => {
  const [roomName, setRoomName] = useState("");
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [chatHistory, setChatHistory] = useState([]);
  const [message, setMessage] = useState("");
  const [sender, setSender] = useState("");
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const [stompClient, setStompClient] = useState(null); // WebSocket client

  const {auth, theme }=useSelector(store=>store);

  useEffect(() => {
    // Connect to WebSocket server
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    const stoconnect = stompClient.connect({}, () => {
      setStompClient(stompClient);
    });

    console.log("stoconnect: ",stoconnect);
  }, []);

  const createRoom = () => {
    // Send a request to create a chat room
    axios.post('http://localhost:8080/createroom', {
      name: roomName,
      creator: auth.user?.fullName,
      creatorEmail: auth.user?.email.split(" ")[0],
    })
    .then((response) => {
      if (response.status === 201) {
        setChatRooms([...chatRooms, response.data]);
      }
    })
    .catch((error) => {
      // Handle error
    });
  };

  // Function to fetch chat rooms
  const fetchChatRooms = () => {
    axios.get('http://localhost:8080/allrooms')
      .then((response) => {
        if (response.status === 200) {
          setChatRooms(response.data);
        }
      })
      .catch((error) => {
        // Handle error
      });
  };

  // Function to enter a chat room
  const enterChatRoom = (roomId, sender) => {
    // Fetch chat history for the room
    axios.post('http://localhost:8080/getchat', roomId, {
      headers: {
        'Content-Type': 'text/plain', // 명시적으로 텍스트 형식을 전송한다는 것을 설정
      },
    })
      .then((response) => {
        if (response.status === 200) {
          setChatHistory(response.data);
          console.log("enter: ", response.data);
        }
      })
      .catch((error) => {
        // Handle error
      });
  
    // Open the chat modal
    setSelectedRoom(roomId);
    setModalIsOpen(true);
  };

  // Function to send a chat message
  const sendMessage = () => {
    if (stompClient) {
      // 보낼 메시지 객체 생성
      const chatMessage = {
        type: "TALK",
        roomId: selectedRoom,
        sender: auth.user?.fullName,
        email: auth.user?.email.split(" ")[0],
        message: message,
      };
      console.log("chatmessage: ",chatMessage);
      
      // WebSocket을 통해 메시지 전송
      stompClient.send(`/app/savechat/${selectedRoom}`, {}, JSON.stringify(chatMessage));
      
      setMessage(''); // 입력 필드 초기화
    } else {
      // stompClient가 없을 때 처리
      console.error("WebSocket connection is not established.");
    }
  }

  useEffect(() => {
    if (stompClient && selectedRoom) {
      const subscription = stompClient.subscribe(`/topic/${selectedRoom}`, (message) => {
        const chatMessage = JSON.parse(message.body);
        console.log("chatMessage: ",chatMessage);
        setChatHistory((prevChatHistory) => [...prevChatHistory, chatMessage]);
      });
      
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [stompClient, selectedRoom]);

  // Fetch chat rooms when the component mounts
  useEffect(() => {
    fetchChatRooms();
  }, []);

  // Render chat rooms and chat history
  return (
    <div>
      <h1>Chat Rooms</h1>
      <input
        type="text"
        placeholder="Enter room name"
        value={roomName}
        onChange={(e) => setRoomName(e.target.value)} // 사용자가 입력한 값을 상태에 업데이트
      />
      <button onClick={createRoom}>Create Chat Room</button> 
      <ul>
        {chatRooms.length > 0 ? (
            chatRooms.map((room) => (
            <li key={room.roomId} onClick={() => enterChatRoom(room.roomId, "sender")}>
                {room.name}
            </li>
            ))
        ) : (
            <li>채팅방이 없습니다</li>
        )}
      </ul>

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={() => setModalIsOpen(false)}
        style={customStyles}
      >
        <h2>Chat Room: {selectedRoom}</h2>
        <div>
          {chatHistory.length > 0 ? (
            chatHistory.map((chat) => (
              <div key={chat.id}>
                {chat.message && (
                  <span
                    style={{
                      display: 'block',
                      textAlign: auth.user?.fullName === chat.sender ? 'right' : 'left',
                    }}
                  >
                    
                    {auth.user?.fullName === chat.sender
                      ? `${chat.message}: ${chat.sender}`
                      : `${chat.sender}: ${chat.message}`}
                  </span>
                )}
              </div>
            ))
          ) : (
            <div>아직 채팅 내역이 없습니다</div>
          )}
        </div>
        <input
          type="text"
          placeholder="Your message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button onClick={sendMessage}>Send</button>
      </Modal>
    </div>
  );
}

export default Chat;
