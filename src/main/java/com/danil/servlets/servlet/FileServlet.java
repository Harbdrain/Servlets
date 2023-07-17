package com.danil.servlets.servlet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.danil.servlets.model.Event;
import com.danil.servlets.model.File;
import com.danil.servlets.model.dto.FileDto;
import com.danil.servlets.repository.hibernate.HibernateEventRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateFileRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateUserRepositoryImpl;
import com.danil.servlets.service.EventService;
import com.danil.servlets.service.FileService;
import com.danil.servlets.service.common.EventServiceImpl;
import com.danil.servlets.service.common.FileServiceImpl;
import com.danil.servlets.utils.ServletUtils;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FileServlet", value = "/files/*")
public class FileServlet extends HttpServlet {
    private final FileService fileService = new FileServiceImpl(new HibernateFileRepositoryImpl(),
            new HibernateEventRepositoryImpl());
    private final EventService eventService = new EventServiceImpl(new HibernateFileRepositoryImpl(),
            new HibernateUserRepositoryImpl(), new HibernateEventRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer fileId = null;
        try {
            fileId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        if (fileId == null) {
            doGetAll(request, response);
        } else {
            doGetSingle(request, response, fileId);
        }
    }

    private void doGetAll(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<File> files = fileService.getAll();
        List<FileDto> filesDto = new ArrayList<>();
        files.forEach(file -> filesDto.add(FileDto.create(file)));

        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(files));
    }

    private void doGetSingle(HttpServletRequest request, HttpServletResponse response, Integer fileId)
            throws IOException, ServletException {
        Integer userId = null;
        try {
            userId = Integer.parseInt(request.getHeader("x-user-id"));
        } catch (NumberFormatException e) {
            response.sendError(400, "Bad parameter");
            return;
        }

        Event event = eventService.create(userId, fileId);
        if (event == null) {
            response.sendError(404);
            return;
        }

        String filePath = event.getFile().getFilePath();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(inputStream.readAllBytes());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer fileId = null;
        try {
            fileId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(400);
            return;
        }

        if (fileId != null) {
            response.sendError(400);
            return;
        }

        String name = request.getParameter("name");
        File file = fileService.create(name);
        if (file == null) {
            response.sendError(400, "bad parameter");
            return;
        }

        try (OutputStream outputStream = new FileOutputStream(file.getFilePath())) {
            ServletInputStream inputStream = request.getInputStream();
            outputStream.write(inputStream.readAllBytes());
        }

        response.getWriter().println(file.getId());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer fileId = null;
        try {
            fileId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        if (fileId == null) {
            response.sendError(400);
            return;
        }

        String name = request.getReader().readLine();
        if (name == null) {
            response.sendError(400, "Bad parameter");
            return;
        }

        File file = fileService.update(fileId, name);
        if (file == null) {
            response.sendError(404);
            return;
        }

        response.getWriter().println("OK");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer fileId = null;
        try {
            fileId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        if (fileId == null) {
            response.sendError(400);
            return;
        }

        fileService.deleteById(fileId);
        response.getWriter().println("OK");
    }
}
