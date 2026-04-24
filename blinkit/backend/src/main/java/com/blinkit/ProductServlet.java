package com.blinkit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.PriorityQueue;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/blinkit_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Parth@13";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CORS Fix: Taaki frontend se data fetch ho sake
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        PriorityQueue<Product> minHeap = new PriorityQueue<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while (rs.next()) {
                minHeap.add(new Product(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("price"), rs.getString("category")
                ));
            }
            conn.close();
        } catch (Exception e) {
            // Agar DB fail hua toh manually data add kar rahe hain testing ke liye
            minHeap.add(new Product(1, "Amul Milk (DB Offline)", 66.0, "Dairy"));
            minHeap.add(new Product(2, "Britannia Bread", 45.0, "Bakery"));
        }

        // Convert Heap to JSON
        StringBuilder json = new StringBuilder("[");
        while (!minHeap.isEmpty()) {
            Product p = minHeap.poll();
            json.append(String.format("{\"id\":%d, \"name\":\"%s\", \"price\":%.2f, \"category\":\"%s\"}",
                    p.getId(), p.getName(), p.getPrice(), p.getCategory()));
            if (!minHeap.isEmpty()) json.append(",");
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }
}