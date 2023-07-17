package com.danil.servlets.model.dto;

import com.danil.servlets.model.User;

public class UserWOEventsDto {
    private Integer id;
    private String name;

    public UserWOEventsDto() {
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

    public static UserWOEventsDto create(User user) {
        UserWOEventsDto dto = new UserWOEventsDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
}
