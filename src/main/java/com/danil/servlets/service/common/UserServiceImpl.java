package com.danil.servlets.service.common;

import java.util.ArrayList;
import java.util.List;

import com.danil.servlets.model.User;
import com.danil.servlets.repository.EventRepository;
import com.danil.servlets.repository.UserRepository;
import com.danil.servlets.service.UserService;

public class UserServiceImpl implements UserService {
    UserRepository userRepository = null;
    EventRepository eventRepository = null;

    public UserServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public User create(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        User user = new User();
        user.setName(name);
        user.setEvents(new ArrayList<>());
        return userRepository.create(user);
    }

    @Override
    public User update(Integer id, String name) {
        if (id == null || name == null || name.isEmpty()) {
            return null;
        }

        User user = userRepository.getByIdLazy(id);
        if (user == null) {
            return null;
        }

        user.setName(name);
        return userRepository.update(user);
    }

    @Override
    public User getById(Integer id) {
        if (id == null) {
            return null;
        }

        return userRepository.getById(id);
    }

    @Override
    public List<User> getAllLazy() {
        return userRepository.getAllLazy();
    }

    @Override
    public void deleteById(Integer id) {
        eventRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
