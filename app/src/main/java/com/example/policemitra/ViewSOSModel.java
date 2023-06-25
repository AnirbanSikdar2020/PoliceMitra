package com.example.policemitra;

public class ViewSOSModel {
    String id;
    String fileNo;
    public ViewSOSModel(String id,String fileNo) {
        this.id = id;
        this.fileNo = fileNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

}
