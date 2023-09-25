import { GoogleOAuthProvider } from '@react-oauth/google';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import { store } from './Store/store';
import './index.css';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
const clientId="770219797693-s1d7pmoo4as6o8gp2ldu75mn5g3oum1t.apps.googleusercontent.com"
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <BrowserRouter>
      <GoogleOAuthProvider clientId={clientId}> <App /> </GoogleOAuthProvider>;
       
      </BrowserRouter>
    </Provider>
  </React.StrictMode>
);

reportWebVitals();
