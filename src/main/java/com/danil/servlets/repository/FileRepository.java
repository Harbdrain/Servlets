package com.danil.servlets.repository;

import java.util.List;

import com.danil.servlets.model.File;

public interface FileRepository extends GenericRepository<File> {
    List<File> getAll();
    void deleteById(Integer id);
}
