
import React, { useState, useEffect } from 'react';
import Board from './Board';
import { connect, subscribeToGameUpdates, sendMove, deactivate } from '../service/websocket';
import { getGameState, joinGame, loggedIn } from '../service/api';
import { redirect, useNavigate } from 'react-router-dom';

const Game = () => {
    const navigate = useNavigate();
    const [gameState, setGameState] = useState(null);
    const [currentPlayer, setCurrentPlayer] = useState('');
    const [playerId, setPlayerId] = useState(null);
    const [opponentFound, setOpponentFound] = useState(false);
    const [gameId, setGameId] = useState(null);
    const [isAuth, setAuth] = useState(false);
    const [showNotification, setShowNotification] = useState(false);
    useEffect(() => {
       checkauth();
        if (opponentFound && gameId) {
            (async () => {
                try {
                    await connect(gameId);
                    onConnected();
                    await initGameState(gameId);
                } catch (error) {
                    console.error('Connection error:', error);
                }
            })();
        }
        // Cleanup on unmount
        return () => {
            deactivate();
        };
    }, [opponentFound, gameId]);
    
    const checkauth = async() =>{
        try {
          const response= await loggedIn();
          if( response.status === 200 && response.data === "COOKIE_IS_VALID"){
            setAuth(true)
          }else{
            navigate("/error")
          }
        } catch (error) {
          navigate("/error")
        }
      }
    const onConnected = () => {
        try {
            subscribeToGameUpdates(gameId, (message) => {
                const receivedGameState = JSON.parse(message.body);
                console.log("New game state",receivedGameState);
                if(receivedGameState.statusCodeValue === 200){
                setGameState(receivedGameState.body);
                }else{
                    setShowNotification(true);
                    setTimeout(()=>{
                        setShowNotification(false);
                        redirect('/')
                    },4000)
                }
            });
        } catch (error) {
            console.log(error);
            setShowNotification(true);
                    setTimeout(()=>{
                        setShowNotification(false);
                        redirect('/')
                    },4000)
        }
    }

    const userJoinsGame = async () => {
        try {
            const response = await joinGame();
            if (response.status === 200) {
                setPlayerId(response.data.playerId);
                setCurrentPlayer(response.data.symbol);
                setGameId(response.data.gameId); // Set the gameId after successfully joining game
                setOpponentFound(true); // Set opponentFound to true after successfully joining game
            } else {
                console.log(response.status);
            }
        } catch (error) {
            console.log("Failed to join a game", error);
        }
    }

    const initGameState = async (gameId) => {
        try {
            const response = await getGameState(gameId);
            if (response.status === 200) {
                setGameState(response.data);
                console.log(response.data);
            } else {
                console.log(response.status);
            }
        } catch (error) {
            console.log("Failed to get game state", error);
        }
    }

    const handleMove = (row, col) => {
        if (gameState && gameState.currentPlayer === currentPlayer && gameState.board[row][col] === '\0') {
            sendMove(gameId, playerId, currentPlayer, row, col);
        }
    };

    const handleFindOpponent = () => {
        userJoinsGame();
    }
   if(!isAuth){
    return;
   }

    return (
        <div className="App flex flex-col items-center justify-center min-h-screen bg-blue-50 p-4">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">Tic-Tac-Toe</h1>
            {!opponentFound && (
                <button onClick={handleFindOpponent} className="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">
                Find Opponent
                </button>
            )}
            {opponentFound && (
                <div>
                    <Board gameState={gameState} onMove={handleMove} />
                    <h2 className="text-2xl font-medium text-gray-700 mt-4 text-center">
                        Your Player: {currentPlayer}
                    </h2>
                    {gameState && gameState.statusMessage && (
                        <h3 className="text-xl text-red-500 mt-2 text-center">{gameState.statusMessage}</h3>
                    )}
                    {gameState && ((gameState.statusMessage === 'X wins!') ||(gameState.statusMessage === 'O wins!')||(gameState.statusMessage === 'Draw!')) && (
                        <div tabIndex="-1" className="fixed inset-0 z-50 flex justify-center items-center bg-gray-800 bg-opacity-50">
                        <div className="relative p-4 w-full max-w-md max-h-full bg-white rounded-lg shadow-lg">
                            <div className="p-4 md:p-5 text-center">
                            <svg className="mx-auto mb-4 text-gray-400 w-12 h-12" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                                <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                            </svg>
                            <h3 className="mb-5 text-lg font-normal text-gray-500">{gameState.statusMessage} Start a new game?</h3>
                            <button onClick={handleFindOpponent} type="button" className="text-white bg-green-600 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center">
                                Yes, start a new game
                            </button>
                            </div>
                        </div>
                        </div>
                    )}
                  
                </div>
            )}
            {!gameState && showNotification && (
                <div className="fixed bottom-5 right-5 bg-green-500 text-white px-4 py-2 rounded">
                    {/* {notifMessage} */}
                    Game Does not Exist, Exceeded Playtime. Redirecting...
                </div>
            )}
            
        </div>

    );
}

export default Game;

