package com.pfe.backend.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String title;
    private String content;
    private String image_url;
    private Date date;
    private Date updatedAt;
    private String username; // Utiliser le username de l'utilisateur qui a créé le post
    private List<Comment> comments; // Add this
    private List<Like> likes;

    public Post(String title, String content, String image_url, Date date, String username) {
        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.date = date;
        this.username = username;
        this.updatedAt = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
