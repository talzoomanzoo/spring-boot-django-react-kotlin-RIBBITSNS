import React, { Component } from 'react';
import WebSocket from 'react-websocket';
import '../../App.css';

class Chat extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: '',  // 사용자의 ID
            message: '',  // 보낼 메시지
            messages: [],  // 수신 및 송신된 메시지를 저장할 배열
            connected: false, // WebSocket 연결 상태
        };
    }
      

  // WebSocket에서 메시지를 처리하는 콜백 함수
  handleMessage = (data) => {
    const message = JSON.parse(data);
    this.setState((prevState) => ({
      messages: [...prevState.messages, message],
    }));
  };

  // 메시지를 보내는 함수
  handleSend = () => {
    const { id, recipientId, message } = this.state;
    const newMessage = { id, recipientId, message };

    // 서버로 메시지를 전송
    this.sendMessageToServer(newMessage);

    // UI 업데이트
    this.setState((prevState) => ({
      messages: [...prevState.messages, newMessage],
      message: '',
    }));
  };

  // WebSocket을 통해 서버로 메시지 전송
  sendMessageToServer = (message) => {
    this.refWebSocket.sendMessage(JSON.stringify(message));
  };

  // Connect 버튼을 클릭하여 WebSocket 연결을 열도록 하는 함수
    connectWebSocket = () => {
        this.setState({ connected: true });
    };

  render() {
    return (
      <div className="App">
        <div>
          <input
            type="text"
            placeholder="Your ID"
            value={this.state.id}
            onChange={(e) => this.setState({ id: e.target.value })}
          />
          <button onClick={this.connectWebSocket}>Connect</button> {/* Connect 버튼 */}
        </div>
        <div>
            {/* ID가 입력되면 Connect 버튼을 누를 수 있도록 변경 */}
            {this.state.id && this.state.connected && (
                <WebSocket
                    url={`ws://localhost:8080/chat/${this.state.id}`}
                    onMessage={this.handleMessage}
                    reconnect={true}
                    debug={true}
                    ref={(Websocket) => {
                        this.refWebSocket = Websocket;
                    }}
                />
            )}
        </div>
        <div>
          <div>
            {this.state.messages.map((message, index) => (
              <div key={index}>
                {message.id}: {message.message}
              </div>
            ))}
          </div>
          <input
            type="text"
            placeholder="Type a message"
            value={this.state.message}
            onChange={(e) => this.setState({ message: e.target.value })}
          />
          <button onClick={this.handleSend}>Send</button>
        </div>

      </div>
    );
  }
}

export default Chat;
