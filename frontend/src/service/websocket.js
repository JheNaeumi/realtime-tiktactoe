
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

/**
 * Connect to the STOMP server.
 * @returns {Promise<void>} A promise that resolves when connected.
 */
export const connect = () => {
    return new Promise((resolve, reject) => {
        const socket = new SockJS('http://localhost:8080/tictactoe');
        stompClient = new Client({
            webSocketFactory: () => socket,
            debug: (str) => console.log(str),
            reconnectDelay: 5000, // Attempt reconnect every 5 seconds
        });

        stompClient.onConnect = () => {
            console.log('Connected to server');
            resolve();
        };

        stompClient.onStompError = (frame) => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
            reject(new Error(frame.headers['message']));
        };

        stompClient.onWebSocketClose = (event) => {
            console.warn('WebSocket connection closed:', event);
        };

        stompClient.onWebSocketError = (event) => {
            console.error('WebSocket error:', event);
            reject(new Error('WebSocket error'));
        };

        stompClient.activate();
    });
};

/**
 * Subscribe to game updates.
 * @param {string} gameId - The game ID to subscribe to.
 * @param {function} callback - The callback to execute when a message is received.
 */
export const subscribeToGameUpdates = (gameId, callback) => {
    if (!callback) {
        console.error('Callback function is required for subscribing to game updates.');
        return;
    }

    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/topic/game/${gameId}`, callback);
    } else {
        stompClient.onConnect = () => {
            stompClient.subscribe(`/topic/game/${gameId}`, callback);
        };
    }
};

/**
 * Send a move to the server.
 * @param {string} gameId - The game ID.
 * @param {string} playerId - The player ID.
 * @param {string} from - The move origin.
 * @param {number} row - The row position of the move.
 * @param {number} col - The column position of the move.
 */
export const sendMove = (gameId, playerId, from, row, col) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: `/app/move/${gameId}`,
            body: JSON.stringify({ playerId, from, row, col }),
        });
    } else {
        console.error('Unable to send move: STOMP client is not connected.');
    }
};

export const deactivate = () => {
    if (stompClient) {
        stompClient.deactivate();
        console.log('STOMP client deactivated');
    }
};