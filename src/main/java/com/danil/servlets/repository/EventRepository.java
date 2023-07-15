package com.danil.servlets.repository;

import com.danil.servlets.model.Event;

public interface EventRepository extends GenericRepository<Event> {
    void deleteByFileId(Integer fileId);
    void deleteByUserId(Integer userId);
}
