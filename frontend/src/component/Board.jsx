import React from 'react';

const Board = ({ gameState, onMove }) => {
    if (!gameState || !gameState.board) {
        return <div className="flex justify-center items-center h-screen">Loading...</div>;
    }

    return (
        <div className="grid gap-1 p-4 bg-gray-100 rounded-lg shadow-md">
            {gameState.board.map((row, rowIndex) => (
                <div key={rowIndex} className="flex">
                    {Array.from(row).map((cell, colIndex) => (
                        <button
                            key={colIndex}
                            className="w-12 h-12 flex items-center justify-center border border-gray-300 rounded hover:bg-gray-200 transition duration-200"
                            onClick={() => onMove(rowIndex, colIndex)}
                        >
                            {cell === '\0' ? '' : cell}
                        </button>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default Board;
