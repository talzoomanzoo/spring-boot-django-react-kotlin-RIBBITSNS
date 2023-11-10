import React from 'react';
import { PacmanLoader } from "react-spinners"


const Loading = () => {
  const loadingStyle = {
    position: 'fixed',  // 화면 고정
    top: '20%',          // 수직 가운데
    left: '50%',         // 수평 가운데
    transform: 'translate(-50%, -50%)', // 화면 중앙으로 이동
    zIndex: 9999,        // 다른 요소보다 위에 표시
    padding: '20px',     // 내용과 로딩 스피너 간격
  };

  return (
    <div style={loadingStyle}>
      <h3 className='text-green-700' >잠시만 기다려주세요</h3>
      <PacmanLoader color="hsla(113, 67%, 43%, 1)" />
    </div>
  );
};

export default Loading;
