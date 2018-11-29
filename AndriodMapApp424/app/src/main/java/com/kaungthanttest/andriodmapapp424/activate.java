package com.kaungthanttest.andriodmapapp424;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/activate", ""})
public class activate extends HttpServlet {
    private Statement stmt;
    private String from = "nicklin1219@gmail.com";
    private String host = "localhost";
    private Connection con;
    Long time = System.currentTimeMillis();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "root", "root");
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }

        processRequest(request, response);
        String email = request.getParameter("email");
        String hash = request.getParameter("key");

        try {
            PreparedStatement pst = con.prepareStatement("SELECT sent_time FROM Vertification " +
                    "WHERE email = ? AND hash = ?");
            pst.setString(1, email);
            pst.setString(2, hash);
            ResultSet rs  = pst.executeQuery();
            if (rs.next() && rs.getInt("sent_time") <= time + 1800000) {
                PreparedStatement pst2 = con.prepareStatement("UPDATE Vertification WHERE " +
                        "email = ? SET type = type - 2");
                pst2.setString(1, email);
                if(pst2.executeUpdate() > 0) {
                    System.out.println("Activated");
//                    response.sendRedirect("");
                }
                else {
                    System.out.println("Active failed");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

