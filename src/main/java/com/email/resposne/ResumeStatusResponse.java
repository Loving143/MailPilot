package com.email.resposne;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumeStatusResponse {
    
    @JsonProperty("hasResume")
    private boolean hasResume;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("fileSize")
    private Long fileSize;
    
    @JsonProperty("lastModified")
    private String lastModified;
    
    @JsonProperty("filePath")
    private String filePath;

    // Constructors
    public ResumeStatusResponse() {}

    public ResumeStatusResponse(boolean hasResume) {
        this.hasResume = hasResume;
    }

    public ResumeStatusResponse(boolean hasResume, String fileName, Long fileSize, String lastModified, String filePath) {
        this.hasResume = hasResume;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.filePath = filePath;
    }

    // Getters and Setters
    public boolean isHasResume() {
        return hasResume;
    }

    public void setHasResume(boolean hasResume) {
        this.hasResume = hasResume;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "ResumeStatusResponse{" +
                "hasResume=" + hasResume +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", lastModified='" + lastModified + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}