import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { searchChatUser } from "../../Store/Auth/Action";
import { useDispatch, useSelector } from "react-redux";
import SearchIcon from "@mui/icons-material/Search";
import { Avatar, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import AddCommentIcon from '@mui/icons-material/AddComment';
import ChatIcon from '@mui/icons-material/Chat';

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

const customeditStyles = {
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
  const style = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 50,
    maxHeight: 50,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 2,
    borderRadius: 3,
    outline: "none",
    overflow: "scroll-y",
  }
  const [roomName, setRoomName] = useState("");
  const [chatRooms, setChatRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [selectedRoomId, setSelectedRoomId] = useState(null);

  const [chatHistory, setChatHistory] = useState([]);
  const [message, setMessage] = useState("");
  const [sender, setSender] = useState("");
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const [stompClient, setStompClient] = useState(null);
  const [error, setError] = useState('');

  const { auth, theme } = useSelector(store => store);

  const [search, setSearch] = useState("");
  const dispatch = useDispatch();

  const [completeCreated, setCompleteCreated] = useState(0);
  const [completeDeleted, setCompleteDeleted] = useState(0);
  const [completeEdited, setCompleteEdited] = useState(0);

  const [finduserrender, setFinduserrender] = useState(0);

  const [editModalIsOpen, setEditModalIsOpen] = useState(false);
  const [selectedEditRoomId, setSelectedEditRoomId] = useState('');
  const [newRoomName, setNewRoomName] = useState('');
  const navigate = useNavigate();
  const handleBack = () => {
    navigate(-1);
  };
  const [modalState, setModalState] = useState(false);

  function OnOffModal() {
    if (modalState === true) {
      setModalState(false);
    } else {
      setModalState(true);
    }
  }


  const [userList, setUserList] = useState([]);

  useEffect(() => {//웹소켓 연결
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    const stoconnect = stompClient.connect({}, () => {
      setStompClient(stompClient);
    });

    console.log("stoconnect: ", stoconnect);
  }, []);

  const createRoom = () => {//채팅방 만드는 함수
    if (!roomName) {
      setError("채팅방 명을 입력해주세요.");//채팅방 명을 안 넣을시 뜨는 함수
      return;
    }

    axios.post('http://localhost:8080/createroom', {//백엔드로 채팅방 관련 소스 보냄
      name: roomName,
      creator: auth.user?.fullName,
      creatorEmail: auth.user?.email.split(" ")[0],
    })
      .then((response) => {
        if (response.status === 201) {//채팅방을 만드면 자동으로 렌더링 해주는 곳
          setChatRooms([...chatRooms, response.data]);//채팅방 내역
          setRoomName("");//채팅방 이름 초기화
          setError("");//에러 메시지 초기화
          setCompleteCreated((prev) => prev + 1);//부분 렌더링
        }
      })
      .catch((error) => {

      });
  };

  const handleSearchChatUser = (event) => {//채팅방에 들어가서 초대할 유저 검색시 이용되는 함수
    setSearch(event.target.value);
    dispatch(searchChatUser(event.target.value));
  };

  const enterChatRoom = (roomId, roomname) => {//채팅방 들어갈때의 함수
    // Fetch chat history for the room
    axios.post('http://localhost:8080/getchat', roomId, {//백엔드로 채팅 정보 보네 이전 채팅 내역 출력
      headers: {
        'Content-Type': 'text/plain',
      },
    })
      .then((response) => {
        if (response.status === 200) {
          setChatHistory(response.data);//해당 방 이전 채팅 내역을 출력
        }
      })
      .catch((error) => {

      });

    setSelectedRoom(roomname);//해당 채팅방 이름
    setSelectedRoomId(roomId);//해당 채팅방 id
    setModalIsOpen(true);//모달 열어둠
  };

  const openEditModal = (roomId, roomname) => {//채팅방 정보 수정이 이용되는 함수
    setSelectedEditRoomId(roomId);//해당 채팅방 id
    setNewRoomName(roomname);//기존 채팅방 이름이자, 새롭게 입력될 채팅방 이름
    setEditModalIsOpen(true);//모달창 오픈
  };

  const saveEditedRoomName = () => {//채팅방 제목 수정시 이용
    axios.post('http://localhost:8080/editroom', {
      roomId: selectedEditRoomId,//기존 채팅방 id와 새롭게 작성한 채팅방 이름을 전송
      name: newRoomName,
    })
      .then((response) => {
        if (response.status === 200) {
          setEditModalIsOpen(false);//모달 닫음
          setCompleteEdited((prev) => prev + 1);//부분 렌더링
        }
      })
      .catch((error) => {
      });
  };

  const sendMessage = () => {//대화할때 이용되는 함수(보낼때)
    if (stompClient) {
      const chatMessage = {//채팅 전송시 같이 전송되는 내역들
        type: "TALK",
        roomId: selectedRoomId,
        sender: auth.user?.fullName,
        email: auth.user?.email.split(" ")[0],
        message: message,
      };
      console.log("sendMessage", sendMessage);

      stompClient.send(`/app/savechat/${selectedRoom}`, {}, JSON.stringify(chatMessage));//채팅 내역 보내는 경로

      setMessage(''); // 채팅 입력창 초기화
    } else {
      console.error("WebSocket connection is not established.");
    }
  }

  const UserAddList = (roomid, useremail, sendername) => {//해당 채팅방에 유저 추가할때 쓰는 함수
    const adduser = {//추가하려는 유저의 정보와 초대할 방의 id전송
      roomId: roomid,
      email: useremail,
      sender: sendername,
    };

    axios.post('http://localhost:8080/addusers', adduser)//유저추가시 보내는 경로
      .then((response) => {
        if (response.status === 201) {
          setFinduserrender((prev) => prev + 1);//유저 내역 부분 랜더링
          setSearch('');//검색창 초기화
        }
      })
      .catch((error) => {

      });
  };

  useEffect(() => {
    if (stompClient && selectedRoom) {//채팅이 오는 것을 받는 함수(받을때)
      const subscription = stompClient.subscribe(`/topic/${selectedRoom}`, (message) => {
        const chatMessage = JSON.parse(message.body);
        setChatHistory((prevChatHistory) => [...prevChatHistory, chatMessage]);
      });

      return () => {
        subscription.unsubscribe();
      };
    }
  }, [stompClient, selectedRoom]);

  const deleteUserInRoom = (roomId) => {//채팅방에서 나갈때 이용되는 함수
    axios
      .post('http://localhost:8080/deleteusers', {//내 이메일과 나가려는 채팅방 id 전송
        email: auth.user?.email.split(" ")[0],
        roomId: roomId,
      })
      .then((response) => {
        if (response.status === 200) {//나가면 부분 렌더링이 되어서 채팅방이 없어짐
          setCompleteDeleted((prev) => prev + 1);
        } else if (response.status === 404) {

        }
      })
      .catch((error) => {
      });
  };

  useEffect(() => {
    axios.get('http://localhost:8080/allrooms').then((response) => {//자신이 소속된 채팅방 출력
      const rooms = response.data;
      console.log("rooms: ", rooms);
      setChatRooms(rooms);

      rooms.forEach((room) => {
        checkUserInRoom(room.roomId);
      });
    }, []);
  }, [completeCreated, completeDeleted, completeEdited]);//부분 렌더링

  useEffect(() => {//해당 채팅방에 누가 소속되 있는지를 출력하는 함수
    if (modalIsOpen) {
      axios
        .post('http://localhost:8080/findusers', selectedRoomId, {
          headers: {
            'Content-Type': 'text/plain',
          },
        })
        .then((response) => {
          setUserList(response.data);
        })
        .catch((error) => {

        });
    }
  }, [modalIsOpen, selectedRoomId, finduserrender]);//부분 렌더링

  const checkUserInRoom = (roomId) => {//채팅방에 들어가 검색해서 유저를 추가할때 이미 있는데 중복되느 말도록 관리해주는 함수
    //이미 본인이 있으면 검색창에서 조회가 안됨
    axios
      .post('http://localhost:8080/finduser', {
        email: auth.user?.email.split(" ")[0],
        roomId: roomId,
      })
      .then((response) => {
        if (response.status === 200) {
          setChatRooms((prevChatRooms) =>
            prevChatRooms.map((room) =>
              room.roomId === roomId ? { ...room, isVisible: true } : room
            )
          );
        } else if (response.status === 404) {
          setChatRooms((prevChatRooms) =>
            prevChatRooms.map((room) =>
              room.roomId === roomId ? { ...room, isVisible: false } : room
            )
          );
        }
      })
      .catch((error) => {

      });
  };

  return (
    <div>
      <section
        className={`z-50 flex items-center sticky top-0 bg-opacity-95`}
      >
        <div className="z-50 flex items-center sticky top-0 space-x-5">
          <KeyboardBackspaceIcon
            className="cursor-pointer"
            onClick={handleBack}
          />
          <h1 className="py-5 text-xl font-bold opacity-90 ml-5">
            Chat Room
          </h1>
        </div>
        <div className="absolute right-0 cursor-pointer"
        >
          <input
            type="text"
            placeholder="채팅방 이름"
            value={roomName}
            onChange={(e) => setRoomName(e.target.value)}
            className={`outline-none text-gray-500 ${theme.currentTheme === "light" ? "bg-stone-300" : "bg-[#151515]"}`}
          />
          <AddCommentIcon onClick={createRoom} />
          채팅방 생성
        </div>
      </section>
      {/* <button onClick={createRoom}>Create Chat Room</button> */}
      {error && (
        <div style={{ color: 'red' }}>{error}</div>
      )}
      <div>
        {chatRooms.length > 0 ? (//채팅방 목록 출력
          chatRooms.map((room) => (
            <div
              key={room.roomId}
              style={{
                padding: '10px',
                marginBottom: '10px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                display: room.isVisible ? 'block' : 'none',
              }}
            >
              <button
                onClick={() => enterChatRoom(room.roomId, room.name)}//채팅방 입장
                style={{ cursor: 'pointer' }}
              >
                <ChatIcon/>
                {room.name}
              </button>
              <div>
                <button
                  onClick={() => openEditModal(room.roomId, room.name)}//채팅방 정보 수정
                  style={{ cursor: 'pointer',marginRight: '5px' }}
                >
                  이름
                </button>
                <button
                  onClick={() => deleteUserInRoom(room.roomId)}//채팅방 나가기
                  style={{ cursor: 'pointer',}}
                >
                  나가기
                </button>
              </div>
            </div>
          ))
        ) : (
          <div>채팅방이 없습니다</div>
        )}
      </div>

      <Modal //채팅방
        isOpen={modalIsOpen}
        onRequestClose={() => setModalIsOpen(false)}
        sx={customStyles}
      >
        <Box>
          <section className={`${theme.currentTheme === "dark" ? "text-black" : "text-black"}`}>Chat Room: {selectedRoom}</section>
          <div>
            <input
              value={search}
              onChange={handleSearchChatUser}//채팅방에 초대할 유저 입력
              type="text"
              placeholder="유저 검색"
              className={`${theme.currentTheme === "dark" ? "text-black" : "text-black"}`}
            />
            {search && (
              <div>
                {auth.userSearchResult && auth.userSearchResult.map((item) => (!userList.some((user) => user.email === item.email) && (
                  //초대할 유저 검색, rightpart에 있는 '사용자 및 글 검색'그대로 가져와 수정함
                  <div key={item.id}>
                    <Avatar
                      alt={item.fullName}
                      src={item.image ? item.image : "https://cdn.pixabay.com/photo/2023/10/24/01/42/art-8337199_1280.png"}
                      loading="lazy"
                    />
                    <div>
                      <p>{item.fullName}</p>
                      <p>{item.email.split(" ").join("_").toLowerCase()}</p>
                    </div>
                    <button
                      onClick={() => {
                        UserAddList(selectedRoomId, item.email.split(" ").join("_").toLowerCase(), item.fullName);
                      }}
                    >
                      추가
                    </button>
                  </div>
                )
                ))}

              </div>
            )}
          </div>
          <div>
            {/* 사용자 목록을 표시 */}
            <ul>
              {userList.map((user) => (
                <li key={user.id}>{user.sender}</li>
              ))}
            </ul>
          </div>
          <div>
            {chatHistory.length > 0 ? (//채팅 내역
              chatHistory.map((chat) => (
                <div key={chat.id}>
                  {chat.message && (
                    <span
                      style={{
                        display: 'block',
                        textAlign: auth.user?.fullName === chat.sender ? 'right' : 'left',//자신의 채팅은 오른쪽, 상대방은 왼쪽
                      }}
                    >

                      {auth.user?.fullName === chat.sender//상대방 채팅은 "이름:채팅", 내 채팅은 "채팅:이름"으로 출력
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
            onChange={(e) => setMessage(e.target.value)}//채팅입력
            className={`${theme.currentTheme === "dark" ? "text-black" : "text-black"}`}
          />
          <button onClick={sendMessage}>Send</button>
        </Box>
      </Modal>

      <Modal //채팅방 정보 수정이 이용되는 모달
        isOpen={editModalIsOpen}
        onRequestClose={() => setEditModalIsOpen(false)}
        style={customeditStyles}
      >
        <h2>Edit Chat Room</h2>
        <input
          type="text"
          value={newRoomName}
          onChange={(e) => setNewRoomName(e.target.value)}//여기에 수정할 이름 입력
        />
        <button onClick={saveEditedRoomName}>Save</button>
        {/* 저장버튼 */}
      </Modal>
    </div>
  );
}

export default Chat;
