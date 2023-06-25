package com.example.policemitra;

public class ViewMyVerificationsModel {
    String id;
    String fileName;
    String details;
    String tile;
    String email;
    String description;

    public ViewMyVerificationsModel(String id, String fileName, String details, String tile, String email, String description) {
        this.id = id;
        this.fileName = fileName;
        this.details = details;
        this.tile = tile;
        this.email = email;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

