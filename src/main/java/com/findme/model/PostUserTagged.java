package com.findme.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "POST_USER_TAGGED")
public class PostUserTagged {
    private Long postId;
    private Long userTagged;

    public PostUserTagged() {
    }

    public PostUserTagged(Long postId, Long userTagged) {
        this.postId = postId;
        this.userTagged = userTagged;
    }

    @Id
    @Column(name = "POST_ID")
    public Long getPostId() {
        return postId;
    }


    @Column(name = "USER_TAGGED_ID")
    public Long getUserTagged() {
        return userTagged;
    }




    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUserTagged(Long userTagged) {
        this.userTagged = userTagged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostUserTagged that = (PostUserTagged) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(userTagged, that.userTagged);
    }

    @Override
    public int hashCode() {

        return Objects.hash(postId, userTagged);
    }

    @Override
    public String toString() {
        return "PostUserTagged{" +
                "postId=" + postId +
                ", userTagged=" + userTagged +
                '}';
    }
}
