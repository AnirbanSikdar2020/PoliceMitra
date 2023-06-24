
package com.example.policemitra;

public class ViewVerificationModel {
    String id;
    String fileNo;
    String tile;
    String description;
    public ViewVerificationModel(String id,String fileNo, String tile, String description) {
        this.id = id;
        this.fileNo = fileNo;
        this.tile = tile;
        this.description = description;
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
    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

}
