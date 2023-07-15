package com.danil.servlets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.danil.servlets.model.Event;
import com.danil.servlets.model.User;
import com.danil.servlets.repository.hibernate.HibernateEventRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateUserRepositoryImpl;
import com.danil.servlets.service.UserService;
import com.danil.servlets.service.common.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    UserService userService = new UserServiceImpl(new HibernateUserRepositoryImpl(),
            new HibernateEventRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            doGetAll(request, response);
            return;
        }

        Integer id = null;
        try {
            id = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(),
                    10);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        doGetSingle(request, response, id);
    }

    private void doGetAll(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<User> users = userService.getAllLazy();

        PrintWriter writer = response.getWriter();

        for (User user : users) {
            writer.println(user.getId() + ": " + user.getName());
        }
    }

    private void doGetSingle(HttpServletRequest request, HttpServletResponse response, Integer id)
            throws IOException, ServletException {
        User user = userService.getById(id);
        if (user == null) {
            response.sendError(404);
            return;
        }

        PrintWriter writer = response.getWriter();
        for (Event event : user.getEvents()) {
            writer.println(event.getFile().getName());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        if (request.getPathInfo() != null && !"/".equals(request.getPathInfo())) {
            response.sendError(400);
            return;
        }

        User user = userService.create(name);
        if (user == null) {
            response.sendError(400, "Bad parameter");
            return;
        }

        response.getWriter().println(user.getId());
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

        User user = userService.update(id, name);
        if (user == null) {
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

        Integer id = null;
        try {
            id = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(), 10);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        userService.deleteById(id);
        response.getWriter().println("OK");
    }
}
