package com.danil.servlets.model.dto;

import com.danil.servlets.model.File;

public class FileDto {
    private Integer id;
    private String name;

    public FileDto() {
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

    public static FileDto create(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setName(file.getName());
        return fileDto;
    }
}
