import React from 'react';

const Board = ({ gameState, onMove }) => {
    if (!gameState || !gameState.board) {
        return <div>Loading...</div>;
    }

    return (
        <div className="board">
            {gameState.board.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {Array.from(row).map((cell, colIndex) => (
                        <button
                            key={colIndex}
                            className="cell"
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
