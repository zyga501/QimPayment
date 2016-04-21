package com.framework.servlet;

import com.framework.ProjectSettings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ProjectInitializationServlet extends HttpServlet {
    public void init() throws ServletException {
        ProjectSettings.Init();
    }
}
