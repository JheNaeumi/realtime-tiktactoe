import React, { useState } from "react";
import Game from "./component/Game";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useEffect } from "react";
import { loggedIn } from "./service/api";

function App() {
  const [isAuth, setAuth] = useState(false);
  useEffect(()=>{
    try {
      const response=  loggedIn();
      if(response.status === 200){
        setAuth(true)
        console.log(isAuth)
      }else{
        console.log(isAuth)
      }
    } catch (error) {
      
    }
  })
  return(
    <>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Game/> && isAuth }></Route>
        </Routes>
    </BrowserRouter>
    </>
  )
}

export default App;
