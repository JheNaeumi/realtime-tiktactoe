import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/api';
const instance = axios.create({
    withCredentials: true,
    baseURL: REST_API_URL
  })


export const getGameState = async (gameId) => {
    return  instance.get(`/gameState?gameId=${gameId}`);
};

export const joinGame = async () => {
    return  instance.get('/joinGame');
};
export const loggedIn =  () => {
    return instance.get ('/loggedIn')
}
