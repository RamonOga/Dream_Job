package main.dream.servlet;

import main.dream.model.Post;
import main.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        System.out.println(req.getParameter("description"));
        Store.instOf().save(new Post(0,
                req.getParameter("name"),
                req.getParameter("description")
                ));
        resp.sendRedirect(req.getContextPath() + "/posts.jsp");
    }
}