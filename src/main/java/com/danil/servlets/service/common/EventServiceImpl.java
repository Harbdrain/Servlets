package com.danil.servlets.service.common;

import com.danil.servlets.model.Event;
import com.danil.servlets.model.File;
import com.danil.servlets.model.User;
import com.danil.servlets.repository.EventRepository;
import com.danil.servlets.repository.FileRepository;
import com.danil.servlets.repository.UserRepository;
import com.danil.servlets.service.EventService;

public class EventServiceImpl implements EventService {
    FileRepository fileRepository = null;
    UserRepository userRepository = null;
    EventRepository eventRepository = null;

    public EventServiceImpl(FileRepository fileRepository, UserRepository userRepository,
            EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Event create(Integer userId, Integer fileId) {
        if (userId == null || fileId == null) {
            return null;
        }

        File file = fileRepository.getById(fileId);
        if (file == null) {
            return null;
        }

        User user = userRepository.getByIdLazy(userId);
        if (user == null) {
            return null;
        }

        Event event = new Event();
        event.setFile(file);
        event.setUser(user);
        return eventRepository.create(event);
    }
}
