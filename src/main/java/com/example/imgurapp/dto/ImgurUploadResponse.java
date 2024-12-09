package com.example.imgurapp.dto;

public class ImgurUploadResponse {

    private int status;
    private boolean success;
    private ImgurData data;

    // Getters and Setters

    public static class ImgurData {
        private String id;
        private String deletehash;
        private String link;
        private String title;
        private String description;

        // Getters and Setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeletehash() {
            return deletehash;
        }

        public void setDeletehash(String deletehash) {
            this.deletehash = deletehash;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ImgurData getData() {
        return data;
    }

    public void setData(ImgurData data) {
        this.data = data;
    }
}
