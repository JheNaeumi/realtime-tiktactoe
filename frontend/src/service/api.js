
//TODO Use Axios
export const getGameState = async () => {
    const response = await fetch('http://localhost:8080/api/gameState');
    return await response.json();
};

export const joinGame = async () => {
    const response = await fetch('http://localhost:8080/api/joinGame');
    if (!response.ok) {
        throw new Error('Failed to join game');
    }
    console.log(response.body)
    return await response.body;
};
