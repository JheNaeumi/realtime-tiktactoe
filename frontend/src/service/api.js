//import axios from 'axios';

// const instance = axios.create({
//     withCredentials: true,
//     baseURL: REST_API_URL
//   })

import axios from 'axios';
const REST_API_URL = 'http://localhost:8080/api';

export const getGameState = async (gameId) => {
    return  axios.get(REST_API_URL+`/gameState?gameId=${gameId}`);
};

export const joinGame = async () => {
    return  axios.get(REST_API_URL+'/joinGame');
};
