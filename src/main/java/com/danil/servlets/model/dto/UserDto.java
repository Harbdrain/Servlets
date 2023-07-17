package com.danil.servlets.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.danil.servlets.model.User;

public class UserDto {
    private Integer id;
    private String name;
    List<String> events;

    public UserDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public static UserDto create(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());

        List<String> events = new ArrayList<>();
        user.getEvents().forEach(event -> events.add(event.getFile().getName()));

        userDto.setEvents(events);

        return userDto;
    }
}
