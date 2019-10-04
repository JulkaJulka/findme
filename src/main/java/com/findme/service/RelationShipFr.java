package com.findme.service;

import com.findme.model.RelationShipFrnds;

public abstract class RelationShipFr {
    private RelationShipFrnds next;

    public RelationShipFrnds RelationShipFr(RelationShipFrnds next) {
        this.next = next;
        return next;
    }
}
