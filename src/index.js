import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./Login";
import Main from "./Main";
import Join from "./Join";
import PostEdit from "./PostEdit";
import PostInfo from "./PostInfo";
import Header from "./Header";
import PostUpdate from "./PostUpdate";
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
      <Header/>
      <BrowserRouter>
          <Routes>
              <Route path="/post" element={<PostEdit/>}/>
              <Route path="/post/:id" element={<PostInfo/>}/>
              <Route path="/post/update/:id" element={<PostUpdate/>}/>
              <Route path="/login" element={<Login />}/>
              <Route path="/join" element={<Join />}/>
              <Route path="/" element={<Main />}/>
          </Routes>
      </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
