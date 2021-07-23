package main.dream.servlet;

import main.dream.LogCreator;
import main.dream.exceptions.AlreadyEmailException;
import main.dream.model.User;
import main.dream.store.PsqlStore;
import main.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = new User(0,
                    req.getParameter("name"),
                    req.getParameter("email"),
                    req.getParameter("password"));
            PsqlStore.instOf().addUser(user);
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } catch (AlreadyEmailException e) {
            LogCreator.getLogger().error("Message from addUser method ", e);
            req.setAttribute("error", "Данный email же зарегистрирован в системе.");
            resp.sendRedirect(req.getContextPath() + "/reg.jsp");
        }
    }
}
