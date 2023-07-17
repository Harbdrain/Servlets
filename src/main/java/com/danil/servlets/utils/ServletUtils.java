package com.danil.servlets.utils;

public class ServletUtils {
    static public Integer getIdFromPathInfo(String pathInfo) throws NumberFormatException {
        if (pathInfo == null || "/".equals(pathInfo) || pathInfo.isEmpty()) {
            return null;
        }
        Integer id;
        id = Integer.parseInt(pathInfo, 1, pathInfo.endsWith("/") ? pathInfo.length() - 1 : pathInfo.length(), 10);
        return id;
    }
}
