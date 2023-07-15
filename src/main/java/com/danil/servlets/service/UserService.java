package com.danil.servlets.service;

import java.util.List;

import com.danil.servlets.model.User;

public interface UserService {
    User create(String name);
    User update(Integer id, String name);
    User getById(Integer id);
    List<User> getAllLazy();
    void deleteById(Integer id);
}
