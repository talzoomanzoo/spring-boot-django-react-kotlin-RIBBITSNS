import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';


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

  useEffect(() => {
    // Connect to WebSocket server
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    const stoconnect = stompClient.connect({}, () => {
      setStompClient(stompClient);
    });

    console.log("stoconnect: ",stoconnect);
  }, []);

  // Subscribe to chat topic
  useEffect(() => {
    if (stompClient) {
      stompClient.subscribe('/topic/chat', (message) => {
        // Handle incoming messages (e.g., chat history)
        const chatMessage = JSON.parse(message.body);
        // Update chatHistory state with new message
        setChatHistory((prevChatHistory) => [...prevChatHistory, chatMessage]);
        console.log("chatMessage: ",chatMessage);
      });
      console.log("stompCLient: ",stompClient);
    }
  }, [stompClient]);

  const createRoom = () => {
    // Send a request to create a chat room
    axios.post('http://localhost:8080/chat/createroom', {
      name: roomName,
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
    axios.get('http://localhost:8080/chat/allrooms')
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
    console.log("roomid: ",roomId);
    console.log("sender: ",sender);
    const enterconnect = stompClient.connect({}, () => {
      // WebSocket 연결이 성공하면 실행
      const enterresponse = stompClient.send('/app/chat/enter', {}, JSON.stringify({
        type: 'ENTER',
        roomId: roomId,
        sender: sender,
      }));
      console.log("enterresponse: ",enterresponse);
      
      const topicresponse = stompClient.subscribe('/topic/' + roomId, (message) => {
        // 서버에서 받은 메시지 처리
        const response = JSON.parse(message.body);
        console.log("enterchat: ",response);
        if (response.status === 200) {
          setChatHistory(response.data);
          setSelectedRoom(roomId);
        }
      });
      console.log("topicresponse: ",topicresponse);

    });
    console.log("enterconnect: ",enterconnect);
    setModalIsOpen(true); // Open the chat modal
  };

  // Function to send a chat message
  const sendMessage = () => {
    // Send a chat message
    axios.post('http://localhost:8080/chat/savechat', {
      type: 'TALK',
      roomId: selectedRoom,
      sender: sender,
      message: message,
    })
    .then((response) => {
      if (response.status === 201) {
        setMessage(''); // Clear the input field
      }
    })
    .catch((error) => {
      // Handle error
    });
  };

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
            <li key={room.roomId} onClick={() => enterChatRoom(room.roomId, sender)}>
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
        <ul>
            {chatHistory.length > 0 ? (
                chatHistory.map((chat) => (
                <li key={chat.id}>{chat.sender}: {chat.message}</li>
                ))
            ) : (
                <li>아직 채팅 내역이 없습니다</li>
            )}
        </ul>
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
