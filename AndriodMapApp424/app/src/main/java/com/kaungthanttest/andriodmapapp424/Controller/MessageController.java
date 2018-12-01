package com.kaungthanttest.andriodmapapp424.Controller;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MessageController {
    private Connection con;

    public MessageController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/app", "root", "root");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<JSONObject> listMessage(int id, double lat, double lon) {
        try {
            Date date = new Date();
            long time = date.getTime();
            ArrayList<JSONObject> list = new ArrayList<>();
            PreparedStatement pst = con.prepareStatement(
                    "SELECT m.id, m.title, m.content FROM messages m, belong b, subcategories sc, " +
                            "subscription s WHERE s.uid = sc.id AND sc.id = b.sid AND b.mid = m.id " +
                            "AND s.uid = ? " +
                            "AND m.start_time < ? AND m.end_time > ?" +
                            "AND m.location_lat - m.location_range < ? " +
                            "AND m.location_lat + m.location_range > ? " +
                            "AND m.location_lon - m.location_range < ? " +
                            "AND m.location_lon + m.location_range > ?"
            );
            pst.setInt(1, id);
            pst.setLong(2, time);
            pst.setLong(3, time);
            pst.setDouble(4, lat);
            pst.setDouble(5, lat);
            pst.setDouble(6, lon);
            pst.setDouble(7, lon);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                JSONObject message = new JSONObject();
                message.put("id", rs.getInt("id"));
                message.put("title", rs.getString("title"));
                message.put("content", rs.getString("content"));
                list.add(message);
            }

            PreparedStatement pst2 = con.prepareStatement(
                    "SELECT id FROM messages WHERE end_time < ?"
            );
            pst2.setLong(1, time);
            ResultSet archive = pst2.executeQuery();
            while (archive.next()) {
                deleteMessageById(archive.getInt("id"));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public JSONObject getMessageById(int id) {
        try {
            JSONObject list = new JSONObject();
            PreparedStatement pst = con.prepareStatement(
                    "SELECT title, content FROM messages"
            );
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.put("title", rs.getString("title"));
                list.put("content", rs.getString("content"));
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int addMessage(int user_id, int subcategory_id, String title, String content,
                          long start_time, long end_time, double location_lat,
                          double location_lon, double location_range) {
        try {
            location_range = location_range / 50;
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO messages(title, content, start_time, end_time, location_lat," +
                            "location_lon, location_range) VALUES(?, ?, ?, ?, ?, ?, ?)"
            );
            pst.setString(1, title);
            pst.setString(2, content);
            pst.setLong(3, start_time);
            pst.setLong(4, end_time);
            pst.setDouble(5, location_lat);
            pst.setDouble(6, location_lon);
            pst.setDouble(7, location_range);
            if (pst.executeUpdate() > 0) {
                int mid = getMessageIdByTitle(title);
                Date date = new Date();
                Long time = date.getTime();
                PreparedStatement pst2 = con.prepareStatement(
                        "INSERT INTO publish(uid, mid, time) VALUES(?, ?, ?)"
                );
                pst2.setInt(1, user_id);
                pst2.setInt(2, mid);
                pst2.setLong(3, time);
                if (pst2.executeUpdate() > 0) {
                    PreparedStatement pst3 = con.prepareStatement(
                            "INSERT INTO belong(mid, sid) VALUES(?, ?)"
                    );
                    pst3.setInt(1, mid);
                    pst3.setInt(2, subcategory_id);
                    if (pst3.executeUpdate() > 0) {
                        return 100;
                    } else {
                        return 601;
                    }
                } else {
                    return 602;
                }
            } else {
                return 603;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 604;
        }
    }

    public int getMessageIdByTitle(String title) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT id FROM messages WHERE title = ?"
            );
            pst.setString(1, title);
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

    public int deleteMessageById(int id) {
        try {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM messages WHERE id = ?"
            );
            pst.setInt(1, id);
            ResultSet message = pst.executeQuery();
            if (!message.next()) {
                return 701;
            }
            PreparedStatement pst2 = con.prepareStatement(
                    "INSERT INTO messages_archive(title, content, start_time, end_time, " +
                            "location_lat, location_lon, location_range) " +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)"
            );
            pst2.setString(1, message.getString("title"));
            pst2.setString(2, message.getString("content"));
            pst2.setLong(3, message.getLong("start_time"));
            pst2.setLong(4, message.getLong("end_time"));
            pst2.setDouble(5, message.getDouble("location_lat"));
            pst2.setDouble(6, message.getDouble("location_lon"));
            pst2.setDouble(7, message.getDouble("location_range"));
            if (pst2.executeUpdate() <= 0) {
                return 702;
            }

            PreparedStatement pst3 = con.prepareStatement(
                    "DELETE FROM messages WHERE id = ?"
            );
            pst3.setInt(1, id);
            if (pst3.executeUpdate() <= 0) {
                return 703;
            }

            PreparedStatement pst4 = con.prepareStatement(
                    "SELECT * FROM publish WHERE mid = ?"
            );
            pst4.setInt(1, id);
            ResultSet publish = pst4.executeQuery();
            if (!publish.next()) {
                return 704;
            }

            PreparedStatement pst5 = con.prepareStatement(
                    "INSERT INTO published(uid, mid, time) VALUES(?, ?, ?)"
            );
            pst5.setInt(1, publish.getInt("uid"));
            pst5.setInt(2, publish.getInt("mid"));
            pst5.setLong(3, publish.getLong("time"));
            if (pst5.executeUpdate() <= 0) {
                return 705;
            }

            PreparedStatement pst6 = con.prepareStatement(
                    "DELETE FROM publish WHERE mid = ?"
            );
            pst6.setInt(1, id);
            if (pst6.executeUpdate() <= 0) {
                return 706;
            }

            PreparedStatement pst7 = con.prepareStatement(
                    "SELECT * FROM belong WHERE mid = ?"
            );
            pst7.setInt(1, id);
            ResultSet belong = pst7.executeQuery();
            if (!belong.next()) {
                return 707;
            }

            PreparedStatement pst8 = con.prepareStatement(
                    "INSERT INTO belonged(mid, sid) VALUES(?, ?)"
            );
            pst8.setInt(1, belong.getInt("mid"));
            pst8.setInt(2, belong.getInt("sid"));
            if (pst8.executeUpdate() <= 0) {
                return 708;
            }

            PreparedStatement pst9 = con.prepareStatement(
                    "DELETE FROM belong WHERE mid = ?"
            );
            pst9.setInt(1, id);
            if (pst9.executeUpdate() <= 0) {
                return 706;
            }
            return 100;
        } catch (Exception e) {
            System.out.println(e);
            return 70;
        }
    }

    public static void main(String[] args) {
        MessageController m = new MessageController();
        System.out.println(m.addMessage(1, 1, "UMD football",
                "UMD football beats Jupiter", 1543628829188L,
                1543638829188L, 38.9897, 76.9378, 25));
    }
}
