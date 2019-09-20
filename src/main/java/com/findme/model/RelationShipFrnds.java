package com.findme.model;

import com.findme.model.RelationShipFriends;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RELATIONSHIP")
public class RelationShipFrnds {
    private Long idRelation;

    private Long userFrom;
    private Long userTo;
    private RelationShipFriends status;
    private Date date_status;

    public RelationShipFrnds() {
    }


    @Id
    @SequenceGenerator(name = "SEQ_REL_STATUS", allocationSize = 1, sequenceName = "SEQ_REL_STATUS")
    @GeneratedValue(generator = "SEQ_REL_STATUS", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID_RELATION")
    public Long getIdRelation() {
        return idRelation;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_STATUS")
    public Date getDate_status() {
        return date_status;
    }

    public void setDate_status(Date date_status) {
        this.date_status = date_status;
    }

    public void setIdRelation(Long idRelation) {
        this.idRelation = idRelation;
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
                "idRelation=" + idRelation +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", status=" + status +
                ", date_status=" + date_status +
                '}';
    }
}
