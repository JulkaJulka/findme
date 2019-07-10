package com.findme.model;

import javax.persistence.*;

@Entity
@Table(name = "RELATIONSHIP")
public class RelationShipFrnds {
    private Long id_relation;

    private Long userFrom;
    private Long userTo;
    private RelationShipFriends status;

    public RelationShipFrnds() {
    }

    @Id
    @SequenceGenerator(name = "SEQ_REL_STATUS", allocationSize = 1, sequenceName = "SEQ_REL_STATUS")
    @GeneratedValue(generator = "SEQ_REL_STATUS", strategy = GenerationType.SEQUENCE)
    public Long getId_relation() {
        return id_relation;
    }

    @Column(name = "USER_FROM")
    public Long getUserFrom() {
        return userFrom;
    }

    @Column(name = "USER_TO")
    public Long getUserTo() {
        return userTo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    public RelationShipFriends getStatus() {
        return status;
    }

    public void setId_relation(Long id_relation) {
        this.id_relation = id_relation;
    }

    public void setUserFrom(Long userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(Long userTo) {
        this.userTo = userTo;
    }

    public void setStatus(RelationShipFriends status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RelationShipFrnds{" +
                "id_relation=" + id_relation +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                '}';
    }
}
