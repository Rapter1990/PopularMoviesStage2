package model;

import java.io.Serializable;

// TODO : 8) Creating Review class with its attributes and implementing Serializable
public class Review implements Serializable{

    // TODO : 9) Defining author of review
    private String author;

    // TODO : 10) Defining reviews of each author
    private String review;

    // TODO : 11) Defining empty constuctor
    public Review() {

    }

    // TODO : 12) Defining a constructor with parameters
    public Review(String author, String review) {
        this.author = author;
        this.review = review;
    }

    // TODO : 13) Getter and Setters method
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
