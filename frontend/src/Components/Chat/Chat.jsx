import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {Modal} from "@mui/material";

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

function Chat() {
  const [roomName, setRoomName] = useState("");
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [chatHistory, setChatHistory] = useState([]);
  const [message, setMessage] = useState("");
  const [sender, setSender] = useState("");
  const [modalIsOpen, setModalIsOpen] = useState(false);

  // Function to create a chat room
  const createRoom = async () => {
    try {
      const response = await axios.post('http://localhost:8080/chat/createroom', {
        name: roomName,
      });
      // Handle response, e.g., update chatRooms state
    } catch (error) {
      // Handle error
    }
  };

  // Function to fetch chat rooms
  const fetchChatRooms = async () => {
    try {
      const response = await axios.get('http://localhost:8080/chat/allrooms');
      // Handle response, e.g., update chatRooms state
    } catch (error) {
      // Handle error
    }
  };

  // Function to enter a chat room
  const enterChatRoom = async (roomId, sender) => {
    try {
      const response = await axios.post('http://localhost:8080/chat/enter', {
        type: 'ENTER',
        roomId:'',
        sender:'',
      });
      // Handle response, e.g., update chatHistory state
      setModalIsOpen(true); // Open the chat modal
      setSelectedRoom(roomId);
    } catch (error) {
      // Handle error
    }
  };

  // Function to send a chat message
  const sendMessage = async () => {
    try {
      const response = await axios.post('http://localhost:8080/chat/savechat', {
        type: 'TALK',
        roomId: selectedRoom,
        sender:'',
        message:'',
      });
      // Handle response, e.g., update chatHistory state
      setMessage(''); // Clear the input field
    } catch (error) {
      // Handle error
    }
  };

  // Fetch chat rooms when the component mounts
  useEffect(() => {
    fetchChatRooms();
  }, []);

  // Render chat rooms and chat history
  return (
    <div>
      <h1>Chat Rooms</h1>
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
