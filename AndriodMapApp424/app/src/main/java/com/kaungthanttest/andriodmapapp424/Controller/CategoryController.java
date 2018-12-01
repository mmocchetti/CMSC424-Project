package com.kaungthanttest.andriodmapapp424.Controller;

import java.sql.*;
import java.util.ArrayList;

public class CategoryController {
    private Connection con;

    public CategoryController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/app", "root", "root");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> listCategories() {
        try {
            ArrayList<String> list = new ArrayList<>();
            PreparedStatement pst = con.prepareStatement(
                    "SELECT name FROM categories"
            );
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int getCategoryIdByName(String name) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT id FROM categories c WHERE c.name = ?"
            );
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public int getSubcategoryIdByName(String name) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT id FROM subcategories sc WHERE sc.name = ?"
            );
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public ArrayList<String> listSubcategories(int id) {
        try {
            ArrayList<String> list = new ArrayList<>();
            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM subcategories sc, sub_of so WHERE sc.id = so.sid AND " +
                            "so.cid = ?"
            );
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int subscribe(int user_id, int sub_id) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO subscription(uid, sid) values(?, ?)"
            );
            pst.setInt(1, user_id);
            pst.setInt(2, sub_id);
            if (pst.executeUpdate() > 0) {
                return 100;
            } else {
                return 501;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 502;
        }
    }

    public int unsubscribe(int user_id, int sub_id) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "DELETE FROM subscription WHERE uid = ? AND sid = ?"
            );
            pst.setInt(1, user_id);
            pst.setInt(2, sub_id);
            if (pst.executeUpdate() > 0) {
                return 100;
            } else {
                return 501;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 502;
        }
    }

    public int addCategories(String name) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO categories(name) values(?)"
            );
            pst.setString(1, name);
            if (pst.executeUpdate() > 0) {
                return 100;
            } else {
                return 503;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 504;
        }
    }

    public int addSubCategories(int cid, String name) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO subcategories(name) values(?)"
            );
            pst.setString(1, name);

            if (pst.executeUpdate() > 0) {
                int sid = getSubcategoryIdByName(name);
                PreparedStatement pst3 = con.prepareStatement(
                        "INSERT INTO sub_of(cid, sid) values(?, ?)"
                );
                pst3.setInt(1, cid);
                pst3.setInt(2, sid);

                if (pst3.executeUpdate() > 0) {
                    return 100;
                } else {
                    return 503;
                }
            } else {
                return 504;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 505;
        }
    }
}
