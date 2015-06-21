package com.github.perfin.service.servlet;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

@WebServlet(name = "LogoutServlet", urlPatterns = { "/logout" }, loadOnStartup = 1)
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session= req.getSession();
        session.invalidate();
        RequestDispatcher rd = req.getRequestDispatcher("/");
        System.out.println("Servlet called!");
        rd.forward(req,res);
    }
}
