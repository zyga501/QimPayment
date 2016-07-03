package com.message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hammer on 2016-07-03.
 */
@WebServlet(name = "NotifyCenterServlet")
public class NotifyCenterServlet extends HttpServlet {

    public void init() throws ServletException {
        NotifyCenter.Make();// NotifyCenter notifyCenter= new NotifyCenter();
    }
}
