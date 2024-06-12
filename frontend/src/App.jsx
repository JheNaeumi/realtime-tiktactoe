import React from "react";
import Game from "./component/Game";
import { BrowserRouter, Route, Routes } from "react-router-dom";

function App() {
  return(
    <>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Game/>}></Route>
        </Routes>
    </BrowserRouter>
    </>
  )
}

export default App;
