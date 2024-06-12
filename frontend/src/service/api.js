import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/api'

export const getGameState = async () => {
    return axios.get(REST_API_URL + '/gameState');
};

export const joinGame = async () => {
    return axios.get(REST_API_URL + '/joinGame');
};
