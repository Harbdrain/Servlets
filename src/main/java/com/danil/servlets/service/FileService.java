package com.danil.servlets.service;

import java.util.List;

import com.danil.servlets.model.File;

public interface FileService {
    File create(String name);
    File update(Integer id, String name);
    List<File> getAll();
    void deleteById(Integer id);
}
