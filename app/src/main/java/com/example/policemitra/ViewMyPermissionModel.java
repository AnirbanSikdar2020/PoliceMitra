
package com.example.policemitra;

public class ViewMyPermissionModel {
    String id;
    String fileName;
    String details;
    String tile;
    String description;

    public ViewMyPermissionModel(String id, String fileName, String details, String tile, String description) {
        this.id = id;
        this.fileName = fileName;
        this.details = details;
        this.tile = tile;
        this.description = description;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

