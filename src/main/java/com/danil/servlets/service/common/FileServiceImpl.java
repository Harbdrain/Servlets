package com.danil.servlets.service.common;

import java.util.List;

import com.danil.servlets.model.File;
import com.danil.servlets.repository.EventRepository;
import com.danil.servlets.repository.FileRepository;
import com.danil.servlets.service.FileService;
import com.danil.servlets.utils.FileUtils;

public class FileServiceImpl implements FileService {
    FileRepository fileRepository = null;
    EventRepository eventRepository = null;

    public FileServiceImpl(FileRepository fileRepository, EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public File create(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        File file = new File();
        file.setName(name);
        long time = System.currentTimeMillis() / 1000L;
        String filePath = FileUtils.FILE_DIR + name + "-" + time;
        file.setFilePath(filePath);
        return fileRepository.create(file);
    }

    @Override
    public File update(Integer id, String name) {
        if (id == null || name == null || name.isEmpty()) {
            return null;
        }

        File file = fileRepository.getById(id);
        if (file == null) {
            return null;
        }

        file.setName(name);
        return fileRepository.update(file);
    }

    @Override
    public List<File> getAll() {
        return fileRepository.getAll();
    }

    @Override
    public void deleteById(Integer id) {
        eventRepository.deleteByFileId(id);
        fileRepository.deleteById(id);
    }
}
