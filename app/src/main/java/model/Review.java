package model;

import java.io.Serializable;

// TODO : 8) Creating Review class with its attributes and implementing Serializable
public class Review implements Serializable{

    // TODO : 111 ) Defining id of review
    private String id;

    // TODO : 9) Defining author of review
    private String author;

    // TODO : 10) Defining reviews of each author
    private String review;

    // TODO : 112 ) Defining url of review
    public String url;

    // TODO : 11) Defining empty constuctor
    public Review() {

    }

    // TODO : 12) Defining a constructor with parameters
    public Review(String id,String author, String review,String url) {
        this.author = author;
        this.review = review;
        this.id = id;
        this.url = url;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return "https://www.themoviedb.org/review/" + getId();
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
