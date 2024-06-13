package com.example.TikTacToe.service;

import com.example.TikTacToe.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    void save(User user);
}
