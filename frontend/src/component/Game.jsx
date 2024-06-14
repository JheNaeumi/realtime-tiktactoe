import React, { useState, useEffect } from 'react';
import Board from './Board';
import { connect, subscribeToGameUpdates, sendMove } from '../service/websocket';
import { getGameState, joinGame } from '../service/api';

const Game = () => {
    const [gameState, setGameState] = useState(null);
    const [currentPlayer, setCurrentPlayer] = useState('');
    const [playerId, setPlayerId] = useState(null);
    const [opponentFound, setOpponentFound] = useState(false);

    useEffect(() => {
        if (opponentFound) {
            // If opponent is found, start WebSocket connection and fetch initial game state
            connect();
            onConnected();
            initGameState();
        }
    }, [opponentFound]);

    const onConnected = () => {
        try {
            subscribeToGameUpdates((message) => {
                const receivedGameState = JSON.parse(message.body);
                setGameState(receivedGameState.body);
            });
        } catch (error) {
            console.log(error);
        }
    }

    const userJoinsGame = async () => {
        try {
            const response = await joinGame();
            if (response.status === 200) {
                setPlayerId(response.data.playerId);
                setCurrentPlayer(response.data.symbol);
                setOpponentFound(true); // Set opponentFound to true after successfully joining game
            } else {
                console.log(response.status);
            }
        } catch (error) {
            console.log("Failed to join a game", error);
        }
    }

    const initGameState = async () => {
        try {
            const response = await getGameState();
            if (response.status === 200) {
                setGameState(response.data);
            } else {
                console.log(response.status);
            }
        } catch (error) {
            console.log("Failed to get game state", error);
        }
    }

    const handleMove = (row, col) => {
        if (gameState && gameState.currentPlayer === currentPlayer && gameState.board[row][col] === '\0') {
            sendMove(playerId, currentPlayer, row, col);
        }
    };

    const handleFindOpponent = () => {
        // Call userJoinsGame to find an opponent
        userJoinsGame();
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
                <h2 className="text-2xl font-medium text-gray-700 mt-4">
                    Current Player: {currentPlayer}
                </h2>
                {gameState && gameState.statusMessage && (
                    <h3 className="text-xl text-red-500 mt-2">{gameState.statusMessage}</h3>
                )}
            </div>
        )}
    </div>
    );
}

export default Game;
