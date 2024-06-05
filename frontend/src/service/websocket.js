import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export const connect = () => {
    const socket = new SockJS('http://localhost:8080/tictactoe');
    stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log(str),
    });

    stompClient.onConnect = () => {
        console.log('Connected to server');
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    stompClient.activate();
};

export const subscribeToGameUpdates = (callback) => {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe('/topic/game', callback);
    } 
    else {
        stompClient.onConnect = () => {
            stompClient.subscribe('/topic/game', callback);
        };
    }
};

export const sendMove = (playerId, from, row, col) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: '/app/move',
            body: JSON.stringify({ playerId, from, row, col }),
        });
    }
};
