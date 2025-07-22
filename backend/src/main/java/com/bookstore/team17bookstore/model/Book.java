package com.bookstore.team17bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Book entity representing a book in the bookstore system.
 */
@Entity
@Table(name = "book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;
    private String category;
    private String author;
    private String title;
    private String coverImageUrl;
    private String edition;
    private String publisher;
    private int publicationYear;
    private int quantityInStock;
    private int minimumThreshold; 
    private double buyingPrice; 
    private double sellingPrice;

    // Default constructor for JPA
    public Book() { }   

    // Constructor with parameters for creating a new book
    public Book(String isbn, String category, String author, String title, String coverImageUrl, String edition,
                String publisher, int publicationYear, int quantityInStock, int minimumThreshold,
                double buyingPrice, double sellingPrice) {
        this.isbn = isbn;
        this.category = category;
        this.author = author;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.edition = edition;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.quantityInStock = quantityInStock;
        this.minimumThreshold = minimumThreshold;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String ISBN) { this.isbn = ISBN; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    public int getMinimumThreshold() { return minimumThreshold; }
    public void setMinimumThreshold(int minimumThreshold) { this.minimumThreshold = minimumThreshold; }

    public double getBuyingPrice() { return buyingPrice; }
    public void setBuyingPrice(double buyingPrice) { this.buyingPrice = buyingPrice; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

}

