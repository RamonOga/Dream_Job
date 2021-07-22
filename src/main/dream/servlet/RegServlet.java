package main.dream.servlet;

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
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email.length() > 1 && password.length() > 1) {
            HttpSession sc = req.getSession();
            User user = new User(0, name, email, password);
            PsqlStore.instOf().addUser(user);
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}
