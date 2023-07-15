package com.danil.servlets.servlet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.danil.servlets.model.Event;
import com.danil.servlets.model.File;
import com.danil.servlets.repository.hibernate.HibernateEventRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateFileRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateUserRepositoryImpl;
import com.danil.servlets.service.EventService;
import com.danil.servlets.service.FileService;
import com.danil.servlets.service.common.EventServiceImpl;
import com.danil.servlets.service.common.FileServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "WebServlet", value = "/files/*")
public class FileServlet extends HttpServlet {
    FileService fileService = new FileServiceImpl(new HibernateFileRepositoryImpl(), new HibernateEventRepositoryImpl());
    EventService eventService = new EventServiceImpl(new HibernateFileRepositoryImpl(),
            new HibernateUserRepositoryImpl(), new HibernateEventRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            doGetAll(request, response);
            return;
        }

        Integer fileId = null;
        try {
            fileId = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(),
                    10);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }
        doGetSingle(request, response, fileId);
    }

    private void doGetAll(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<File> files = fileService.getAll();
        PrintWriter writer = response.getWriter();
        for (File file : files) {
            writer.println(file.getId() + ": " + file.getName());
        }
    }

    private void doGetSingle(HttpServletRequest request, HttpServletResponse response, Integer fileId)
            throws IOException, ServletException {
        Integer userId = null;
        try {
            userId = Integer.parseInt(request.getParameter("user_id"));
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
        if (request.getPathInfo() != null && !"/".equals(request.getPathInfo())) {
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
        if (pathInfo == null || "/".equals(pathInfo)) {
            response.sendError(400);
            return;
        }

        Integer id = null;
        try {
            id = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(), 10);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        String name = request.getReader().readLine();
        if (name == null) {
            response.sendError(400, "Bad parameter");
            return;
        }

        File file = fileService.update(id, name);
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
        if (pathInfo == null || "/".equals(pathInfo)) {
            response.sendError(400);
            return;
        }

        Integer fileId = null;
        try {
            fileId = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(),
                    10);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        fileService.deleteById(fileId);
        response.getWriter().println("OK");
    }
}
