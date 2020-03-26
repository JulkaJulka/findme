package com.findme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "POST")
public class Post {
    private Long id;
    private String message;
    private Date datePosted;
    private String location;
    private Set<User> usersTagged = new HashSet<>();
    private User userPosted;
    private User userPagePosted;

    //TODO
    //levels permissions

    //TODO
    //comments

    public Post() {
    }

    @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "POST_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_POST")
    public Long getId() {
        return id;
    }

    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "DATE_POSTED")
    public Date getDatePosted() {
        return datePosted;
    }
    @Column (name = "LOCATION")
    public String getLocation() {
        return location;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "POST_USER_TAGGED", joinColumns =
            {@JoinColumn(name = "POST_ID")}, inverseJoinColumns = {@JoinColumn(name = "USER_TAGGED_ID")})
    public Set<User> getUsersTagged() {
        return usersTagged;
    }

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_POSTED_ID")
    public User getUserPosted() {
        return userPosted;
    }

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_PAGE_POSTED_ID")
    public User getUserPagePosted() {
        return userPagePosted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsersTagged(Set<User> usersTagged) {
        this.usersTagged = usersTagged;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    public void setUserPagePosted(User userPagePosted) {
        this.userPagePosted = userPagePosted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(message, post.message) &&
                Objects.equals(datePosted, post.datePosted) &&
                Objects.equals(location, post.location) &&
                Objects.equals(userPosted, post.userPosted) &&
                Objects.equals(userPagePosted, post.userPagePosted);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, message, datePosted, location, userPosted, userPagePosted);
    }

/* @Id
    @SequenceGenerator(name = "POST_SEQ", sequenceName = "POST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "POST_SEQ", strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "DATE_POSTED")
    public Date getDatePosted() {
        return datePosted;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_POSTED", nullable = false)
    public User getUserPosted() {
        return userPosted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }*/

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", datePosted=" + datePosted +
                ", location='" + location + '\'' +
                '}';
    }
}
