package com.kaungthanttest.andriodmapapp424.Controller;

import org.json.JSONObject;
import java.sql.*;

public class UserController {
    private Connection con;

    public UserController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/app", "root", "root");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public JSONObject getUserById(int id) {
        try {
            JSONObject user = new JSONObject();
            PreparedStatement pst = con.prepareStatement(
                    "SELECT name, username, email, type FROM users u WHERE u.id = ?"
            );
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                user.put("name", rs.getString("name"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("type", rs.getInt("type"));
            }
            return user;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int deleteUserById(int id) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM users WHERE id = ?"
            );
            pst.setInt(1,id);
            ResultSet user = pst.executeQuery();
            if (!user.next()) {
                return 801;
            }

            PreparedStatement pst2 = con.prepareStatement(
                    "INSERT INTO users_archive(name, email, address, username, password, type) " +
                            "VALUES(?, ?, ?, ?, ?, ?)"
            );
            pst2.setString(1, user.getString("name"));
            pst2.setString(2, user.getString("email"));
            pst2.setString(3, user.getString("address"));
            pst2.setString(4, user.getString("username"));
            pst2.setString(5, user.getString("password"));
            pst2.setInt(6, user.getInt("type"));
            if (pst2.executeUpdate() <= 0) {
                return 802;
            }

            PreparedStatement pst3 = con.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );
            pst3.setInt(1, id);
            if (pst3.executeUpdate() <= 0) {
                return 803;
            }
            return 100;
        } catch (Exception e) {
            System.out.println(e);
            return 804;
        }
    }
}
