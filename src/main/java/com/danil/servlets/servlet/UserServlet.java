package com.danil.servlets.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.danil.servlets.model.User;
import com.danil.servlets.model.dto.UserDto;
import com.danil.servlets.model.dto.UserWOEventsDto;
import com.danil.servlets.repository.hibernate.HibernateEventRepositoryImpl;
import com.danil.servlets.repository.hibernate.HibernateUserRepositoryImpl;
import com.danil.servlets.service.UserService;
import com.danil.servlets.service.common.UserServiceImpl;
import com.danil.servlets.utils.ServletUtils;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", value = "/users/*")
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl(new HibernateUserRepositoryImpl(),
            new HibernateEventRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer userId = null;
        try {
            userId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        if (userId == null) {
            doGetAll(request, response);
        } else {
            doGetSingle(request, response, userId);
        }
    }

    private void doGetAll(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<User> users = userService.getAllLazy();
        List<UserWOEventsDto> dtos = new ArrayList<>();
        users.forEach(user -> dtos.add(UserWOEventsDto.create(user)));

        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(dtos));
    }

    private void doGetSingle(HttpServletRequest request, HttpServletResponse response, Integer id)
            throws IOException, ServletException {
        User user = userService.getById(id);
        if (user == null) {
            response.sendError(404);
            return;
        }

        UserDto userDto = UserDto.create(user);

        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(userDto));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Integer userId = null;
        try {
            userId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(400);
            return;
        }

        if (userId != null) {
            response.sendError(400);
            return;
        }

        String name = request.getParameter("name");
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
        Integer userId = null;
        try {
            userId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }
        if (userId == null) {
            response.sendError(400);
            return;
        }

        String name = request.getReader().readLine();
        if (name == null) {
            response.sendError(400, "Bad parameter");
            return;
        }

        User user = userService.update(userId, name);
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
        Integer userId = null;
        try {
            userId = ServletUtils.getIdFromPathInfo(pathInfo);
        } catch (NumberFormatException e) {
            response.sendError(404);
            return;
        }

        if (userId != null) {
            response.sendError(400);
            return;
        }

        userService.deleteById(userId);
        response.getWriter().println("OK");
    }
}
