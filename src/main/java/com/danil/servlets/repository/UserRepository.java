package com.danil.servlets.repository;

import java.util.List;

import com.danil.servlets.model.User;

public interface UserRepository extends GenericRepository<User> {
    List<User> getAllLazy();
    void deleteById(Integer id);
}
