package com.danil.servlets.service;

import com.danil.servlets.model.Event;

public interface EventService {
    Event create(Integer userId, Integer fileId);
}
