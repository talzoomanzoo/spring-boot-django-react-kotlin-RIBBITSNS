import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ChatList = ({ joinChatRoom }) => {
  const [newRoomName, setNewRoomName] = useState('');
  const [chatRooms, setChatRooms] = useState([]); // chatRooms 상태를 빈 배열로 초기화

  useEffect(() => {
    // 채팅방 목록을 서버에서 가져오는 함수
    const fetchChatRooms = async () => {
      try {
        const response = await axios.get('http://localhost:8080/allrooms');
        const chatRoomsData = response.data;
        // chatRoomsData를 chatRooms 상태로 설정
        setChatRooms(chatRoomsData);

      } catch (error) {
        console.error('Error fetching chat rooms:', error);

      }
    };

    fetchChatRooms();
  }, []);

  const handleCreateChatRoom = async () => {
    try {
      const response = await axios.post('http://localhost:8080/createroom', {
        name: newRoomName,
      });
      const newChatRoom = response.data;
      setChatRooms([...chatRooms, newChatRoom]);
      setNewRoomName(''); // Clear the input field
    } catch (error) {
      console.error('Error creating chat room:', error);
    }
  };

  return (
    <div>
      <h2>Chat Rooms</h2>
      <ul>
        {chatRooms.map((room) => (
          <li key={room.id}>
            <button onClick={() => joinChatRoom(room.id)}>{room.name}</button>
          </li>
        ))}
      </ul>
      <div>
        <input
          type="text"
          placeholder="Enter room name"
          value={newRoomName}
          onChange={(e) => setNewRoomName(e.target.value)}
        />
        <button onClick={() => handleCreateChatRoom(newRoomName)}>Create Room</button>
      </div>
    </div>
  );
};

export default ChatList;
