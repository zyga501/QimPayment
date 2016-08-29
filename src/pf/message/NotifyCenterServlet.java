package pf.message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "NotifyCenterServlet")
public class NotifyCenterServlet extends HttpServlet {
    public void init() throws ServletException {
        NotifyCenter.Make();// NotifyCenter notifyCenter= new NotifyCenter();
    }
}
