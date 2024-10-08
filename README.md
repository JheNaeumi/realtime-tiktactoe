# Realtime-TikTacToe

## Overview
A Realtime Tiktactoe match making game using websockets, and Oauth 2.0 for user auhtenttication. The application is built with react for frontend, spring boot for the backend and MySQL for the database.

## Features

- **Real-time Gameplay**: Experience instant updates during matches using WebSockets.
- **OAuth 2.0 Authentication**: Secure user login with OAuth 2.0 integration.
- **Matchmaking**: Automated matchmaking for players to quickly find opponents.
- **Persistent Data**: Game statistics and user data are stored securely in a MySQL database.
- **Responsive Design**: Optimized for both desktop and mobile devices.

## Dependencies

### Backend (Spring Boot)
- `spring-boot-starter-data-jpa`: Provides JPA functionalities for database access.
- `spring-boot-starter-oauth2-client`: Enables OAuth 2.0 authentication.
- `spring-boot-starter-security`: Adds security features to the application.
- `spring-boot-starter-validation`: Provides validation support.
- `spring-boot-starter-web`: For building RESTful web services.
- `spring-boot-starter-websocket`: Enables WebSocket support for real-time communication.
- `mysql-connector-j`: MySQL JDBC driver for database connectivity.
- `lombok`: Reduces boilerplate code by generating getter/setter methods automatically.
- `spring-boot-starter-test`: For testing the Spring Boot application.
- `spring-security-test`: Provides testing support for Spring Security.
- `spring-boot-starter-thymeleaf`: For server-side rendering (optional).
- `mockito-core`: For mocking in tests.
- `mapstruct`: For automatic mapping between Java objects.

### Frontend (React)
- React: A JavaScript library for building user interfaces.
- Axios: For making HTTP requests to the backend.
- WebSockets: For enabling real-time communication between the server and client.

## Setup

### Prerequisites
- **Java 17** or later
- **Node.js** with npm or Yarn
- **MySQL** database

### Backend Setup

1. Clone the repository:
   ```bash
   https://github.com/JheNaeumi/realtime-tiktactoe.git
   cd realtime-tictactoe/backend
   ```

2. Configure the MySQL database:
   - Create a database named `tictactoe_db`.
   - Update the `application.properties` or `application.yml` file with your MySQL credentials.

3. Build and run the Spring Boot application:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```

2. Install the dependencies:
   ```bash
   npm install
   ```

3. Start the React development server:
   ```bash
   npm run dev
   ```

## Usage

1. Open the application in your browser:
   ```bash
   http://localhost:3000
   ```

2. Register or log in using OAuth 2.0 (e.g., Google, GitHub).

3. Start or join a Tic-Tac-Toe match and enjoy real-time gameplay!

