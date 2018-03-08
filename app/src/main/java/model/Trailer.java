package model;

import java.io.Serializable;


// TODO : 1) Creating Trailer class with its attributes and implementing Serializable
public class Trailer implements Serializable {

    // TODO : 2) Defining trailer ID
    private String id;
    // TODO : 3) Defining trailer name
    private String name;
    // TODO : 4) Defining trailer key
    private String key;
    // TODO : 84 ) Defining trailer type
    private String type;

    // TODO : 5) Defining empty constuctor
    public Trailer() {

    }

    // TODO : 6) Defining a constructor with parameters
    public Trailer(String id, String name, String key,String type) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.type = type;
    }

    // TODO : 7) Getter and Setters method
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

