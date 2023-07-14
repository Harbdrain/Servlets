package com.danil.servlets.service;

import com.danil.servlets.model.User;

public interface UserService {
    User create(String name);
    User update(Integer id, String name);
    User getById(Integer id);
}
