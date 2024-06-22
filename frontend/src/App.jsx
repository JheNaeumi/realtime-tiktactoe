import React, { useState } from "react";
import Game from "./component/Game";
import Error from "./component/Error";
import { BrowserRouter, Route, Routes } from "react-router-dom";


function App() {

  return(
    <>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Game/>}></Route>
            <Route path="/error" element={<Error/>}></Route>
        </Routes>
    </BrowserRouter>
    </>
  )
}

export default App;
