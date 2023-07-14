package com.danil.servlets.service;

import com.danil.servlets.model.File;

public interface FileService {
    File create(String name);
    File update(Integer id, String name);
}
