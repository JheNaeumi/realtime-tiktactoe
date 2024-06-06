import React, { useState, useEffect } from 'react';
import Board from './component/Board';
import { connect, subscribeToGameUpdates, sendMove } from './service/websocket';
import { getGameState, joinGame } from './service/api';
import axios from 'axios';
function App() {
    const [gameState, setGameState] = useState(null);
    const [currentPlayer, setCurrentPlayer] = useState('');
    const [playerId, setPlayerId] = useState(null);

    useEffect(() => {
        connect();
        //TODO: More Readable Use Try Catch
        subscribeToGameUpdates((message) => {
            const receivedGameState = JSON.parse(message.body);
            console.log("Received game state:", receivedGameState.body);
            setGameState(receivedGameState.body);
            console.log("New State",gameState);
        });

        //TODO: Make Separate Service
        axios.get('http://localhost:8080/api/joinGame')
        .then(response => {
            console.log(response); // Log the entire response
            const dto = response.data;
            
            console.log(dto);
            setPlayerId(dto.playerId);
            setCurrentPlayer(dto.symbol);
        })
        .catch(error => {
            console.error("Failed to join game:", error);
        });
    
        //TODO: More Readable Use Try Catch
        // Fetch initial game state
        getGameState().then(initialState => {
            console.log("Initial game state:", initialState);
            setGameState(initialState);
        }).catch(error => {
            console.error("Failed to fetch initial game state:", error);
        });
        console.log(playerId)
    }, []);
    useEffect(() => {
        console.log("New State:", gameState);
    }, [gameState]);

    const handleMove = (row, col) => {
        if (gameState && gameState.currentPlayer === currentPlayer && gameState.board[row][col] === '\0') {
            sendMove(playerId, currentPlayer, row, col);
            console.log("done")
        }
    };

    return (
        <div className="App flex flex-col items-center justify-center min-h-screen bg-blue-50 p-4">
            <h1 className="text-4xl font-bold text-gray-800 mb-4">Tic-Tac-Toe</h1>
                {gameState && <Board gameState={gameState} onMove={handleMove} />}
            <h2 className="text-2xl font-medium text-gray-700 mt-4">
                Current Player: {currentPlayer}
            </h2>
            {gameState && gameState.statusMessage && (
                <h3 className="text-xl text-red-500 mt-2">{gameState.statusMessage}</h3>
            )}
        </div>
    );
}

export default App;
