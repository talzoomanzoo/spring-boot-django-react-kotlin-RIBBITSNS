import React, { useEffect, useState } from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';

var stompClient =null;//websocket 클라 객체를 전역 변수로 선언

const ChatRoom = () => {
    //상태 변수 초기화
    const [privateChats, setPrivateChats] = useState(new Map()); // 1:1 채팅 메시지를 저장    
    const [publicChats, setPublicChats] = useState([]); // 공개 채팅 메시지를 저장
    const [tab,setTab] =useState("CHATROOM");// 현재 선택된 채팅 탭
    const [userData, setUserData] = useState({
        username: '',// 사용자 이름
        receivername: '',// 수신자 이름 (1:1 채팅일 때 사용)
        connected: false,// WebSocket 연결 상태
        message: ''// 사용자가 입력한 메시지
      });
    useEffect(() => {// userData 객체가 변경될 때마다 콘솔에 로그 출력
      console.log(userData);
    }, [userData]);

    // WebSocket 연결을 설정하고 서버에 연결하는 함수
    const connect =()=>{
        let Sock = new SockJS('http://localhost:8080/ws');// 서버 WebSocket 엔드포인트
        stompClient = over(Sock);
        stompClient.connect({},onConnected, onError);
    }

    // WebSocket 연결이 성공한 후 호출되는 함수
    const onConnected = () => {
        setUserData({...userData,"connected": true});// 연결 상태를 true로 업데이트
        stompClient.subscribe('/chatroom/public', onMessageReceived);// 공개 채팅 구독
        stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage);// 1:1 채팅 구독
        userJoin();// 사용자가 채팅에 참여
    }

    // 사용자가 채팅에 참여할 때 호출되는 함수
    const userJoin=()=>{
          var chatMessage = {
            senderName: userData.username,
            status:"JOIN"
          };
          stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }

    // 공개 채팅 및 1:1 채팅 메시지를 수신한 경우 호출되는 함수
    const onMessageReceived = (payload)=>{
        var payloadData = JSON.parse(payload.body);
        switch(payloadData.status){
            case "JOIN":
                if(!privateChats.get(payloadData.senderName)){
                    privateChats.set(payloadData.senderName,[]);
                    setPrivateChats(new Map(privateChats));
                }
                break;
            case "MESSAGE":
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
        }
    }
    
    // 1:1 채팅 메시지를 수신한 경우 호출되는 함수
    const onPrivateMessage = (payload)=>{
        console.log(payload);
        var payloadData = JSON.parse(payload.body);
        if(privateChats.get(payloadData.senderName)){
            privateChats.get(payloadData.senderName).push(payloadData);
            setPrivateChats(new Map(privateChats));
        }else{
            let list =[];
            list.push(payloadData);
            privateChats.set(payloadData.senderName,list);
            setPrivateChats(new Map(privateChats));
        }
    }

    // WebSocket 연결 중 오류가 발생한 경우 호출되는 함수
    const onError = (err) => {
        console.log(err);
    }

    // 사용자가 입력한 메시지를 처리하는 함수
    const handleMessage =(event)=>{
        const {value}=event.target;
        setUserData({...userData,"message": value});
    }

    // 공개 채팅 메시지를 서버에 전송
    const sendValue=()=>{
        if (stompClient) {
            var chatMessage = {
            senderName: userData.username,
            message: userData.message,
            status:"MESSAGE"
            };
            console.log(chatMessage);
            stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
            setUserData({...userData,"message": ""});
        }
    }

    // 사용자 이름을 입력하는 함수
    const handleUsername=(event)=>{
        const {value}=event.target;
        setUserData({...userData,"username": value});
    }

    // 사용자를 서버에 등록하고 연결 설정
    const registerUser=()=>{
        connect();
    }
    return (
    
    <div>
        {userData.connected?
        <div>
            {tab==="CHATROOM" && <div >
                <ul >
                    {publicChats.map((chat,index)=>(
                        <li key={index}>
                            <div style={{ textAlign: chat.senderName !== userData.username ? 'left' : 'right' }}>
                                {chat.senderName}:{chat.message}
                            </div>
                        </li>
                    ))}
                </ul>

                <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <input type="text" placeholder="enter the message" value={userData.message} onChange={handleMessage} style={{ flex: 1 }}/> 
                    <button type="button" onClick={sendValue}>send</button>
                </div>
            </div>}
        </div>
        :
        <div>
            <input
                id="user-name"
                placeholder="Enter your name"
                name="userName"
                value={userData.username}
                onChange={handleUsername}
                margin="normal"
              />
            <button type="button" onClick={registerUser}>
                connect
            </button> 
        </div>}
    </div>
    )
}

export default ChatRoom