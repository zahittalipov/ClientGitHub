package com.angelectro.zahittalipov.clientgithub.entity;

import com.google.gson.annotations.SerializedName;

public class ItemCommit {
    @SerializedName("author")
    private Author author;
    @SerializedName("message")
    private String message;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Author {
        @SerializedName("name")
        private String name;
        @SerializedName("date")
        private String date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
