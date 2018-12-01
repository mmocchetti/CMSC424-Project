package com.kaungthanttest.andriodmapapp424.Controller;

import java.sql.*;
import java.util.*;
import java.security.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.math.*;

public class LoginController {
    private Connection con;
    private Session session;

    public LoginController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/app", "root", "root");

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");
            session = Session.getDefaultInstance(properties, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public int signUp(String name, final String email, String address, String username,
                      String password, int type) {
        String emailHost = "in-v3.mailjet.com";
        String fromUser = "e2235e74b110469defa3cee7c987094e";
        String fromUserEmailPassword = "f198f3ab9ad6373b9bf3d2278bd7eaf2";
        try {
            if (type == 1) { // publisher
                type = 3;
            } else if (type == 2) { // subscriber
                type = 4;
            }
            password = md5(password);

            Random random = new Random();
            random.nextInt(999999);
            final String myhash = md5("" + random);

            PreparedStatement pst = con.prepareStatement("INSERT INTO users(name, email, " +
                    "address, username, password, type)" +
                    " values(?,?,?,?,?,?)");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, address);
            pst.setString(4, username);
            pst.setString(5, password);
            pst.setInt(6, type);

            if (pst.executeUpdate() <= 0) {
                return 401;
            } else {
                try {
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);
                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress("nicklin1219@gmail.com"));
                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                    // Set Subject: header field
                    message.setSubject("Registration on Message Viewer");
                    String url = "http://localhost/activate.php?email=" + email + "&hash=" +
                            myhash;
                    String html = "Click <a href='" + url + "'>here</a> to verify your email or " +
                            "copy the link below: <br>" + url + " <br>into your browser. The " +
                            "link is valid for 30 minutes.";
                    // Now set the actual message
                    message.setText(html, "UTF-8", "html");

                    Transport transport = session.getTransport("smtp");
                    transport.connect(emailHost, fromUser, fromUserEmailPassword);
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    System.out.println("Sent message successfully....");

                    Long time = System.currentTimeMillis();

                    PreparedStatement pst2 = con.prepareStatement("INSERT INTO verification" +
                            "(email, hash, sent_time) values(?,?,?)");
                    pst2.setString(1, email);
                    pst2.setString(2, myhash);
                    pst2.setLong(3, time);

                    if (pst2.executeUpdate() <= 0) {
                        return 402;
                    }
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                    return 403;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return 404;
        }
        return 100;
    }

    public int logIn(String username, String password) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT id, password FROM users WHERE " +
                    "username = ? and type <= 2");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getString("password").equals(md5(password))){
                return rs.getInt("id");
            }
            else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    private String md5(String input) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        Login LoginController = LoginController();
////        System.out.println(login.signUp("Nick Lin","1219nicklin@gmail.com", "college park",
////                "nicklin", "123", 1));
//        System.out.println(login.logIn("nicklin", "23"));
//    }
}