package com.findme;

import java.util.Objects;

public class Article {
    private String link;

    public Article(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(link, article.link);
    }

    @Override
    public int hashCode() {

        return Objects.hash(link);
    }

    @Override
    public String toString() {
        return "Article{" +
                "link='" + link + '\'' +
                '}';
    }
}
