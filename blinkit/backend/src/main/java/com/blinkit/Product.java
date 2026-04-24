package com.blinkit;

public class Product implements Comparable<Product> {
    private int id;
    private String name;
    private double price;
    private String category;
    private String imageurl;

    public Product(int id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters for JSON conversion
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }
}