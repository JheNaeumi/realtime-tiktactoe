import React, { useState, useEffect } from 'react';
import Board from './Board';
import { connect, subscribeToGameUpdates, sendMove } from '../service/websocket';
import { getGameState, joinGame } from '../service/api';

const Game = () => {
    const [gameState, setGameState] = useState(null);
    const [currentPlayer, setCurrentPlayer] = useState('');
    const [playerId, setPlayerId] = useState(null);

    useEffect(() => {
        // Connect new client 
        connect();

        // Connected
        onConnected();

        // User joins game
        userJoinsGame();
    
        // Fetch initial game state
        initGameState();
    }, []);
    const onConnected = () => {
        try {
            subscribeToGameUpdates((message) => {
                const receivedGameState = JSON.parse(message.body);
                console.log("Received game state:", receivedGameState.body);
                setGameState(receivedGameState.body);
                console.log("New State",gameState);
            });
        } catch (error) {
            console.log(error)
        }
        
    }

    const userJoinsGame = async() => {
        try {
            const response = await joinGame();
            if(response.status===200){
                setPlayerId(response.data.playerId);
                setCurrentPlayer(response.data.symbol);
            }else{
                console.log(response.status)
            }
        } catch (error) {
            console.log("Failed to join a game" ,error);
        }
      
        
    }
    const initGameState = async() => {
        try {
            const response = await getGameState();
            if(response.status===200){
                setGameState(response.data)
            }else{
                console.log(response.status)
            }
        } catch (error) {
            console.log("Failed to get game state", error);
        }
    }

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
export default Game;