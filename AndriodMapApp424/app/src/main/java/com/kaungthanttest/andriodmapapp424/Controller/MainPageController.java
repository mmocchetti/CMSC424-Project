package com.kaungthanttest.andriodmapapp424.Controller;

import org.json.JSONObject;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class MainPageController {
    private Connection con;

    public MainPageController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/app", "root", "root");

        } catch (Exception e) {
            System.out.println(e);
        }
    }



//    public static void main(String[] args) {
//        MainPageController m = new MainPageController();
//        System.out.println(m.listMessage(1, 38.970, 76.940));
//    }
}
