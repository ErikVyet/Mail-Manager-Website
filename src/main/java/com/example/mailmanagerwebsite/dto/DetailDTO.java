package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.embedded.DetailId;
import com.example.mailmanagerwebsite.model.Detail;

public class DetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private DetailId id;
    private boolean seen;
    private boolean starred;
    private boolean trashed;
    private LocalDateTime received;

    public DetailId getId() {
        return id;
    }

    public void setId(DetailId id) {
        this.id = id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    public LocalDateTime getReceived() {
        return received;
    }

    public void setReceived(LocalDateTime received) {
        this.received = received;
    }

    public static DetailDTO convert(Detail detail) {
        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setSeen(detail.isSeen());
        detailDTO.setStarred(detail.isStarred());
        detailDTO.setTrashed(detail.isTrashed());
        detailDTO.setReceived(detail.getReceived());
        return detailDTO;
    }

    public static Detail revert(DetailDTO detailDTO) {
        Detail detail = new Detail();
        detail.setId(detailDTO.getId());
        detail.setSeen(detailDTO.isSeen());
        detail.setStarred(detailDTO.isStarred());
        detail.setTrashed(detailDTO.isTrashed());
        detail.setReceived(detailDTO.getReceived());
        return detail;
    }

    @Override
    public String toString() {
        return String.format("{ id: %s, seen: %b, starred: %b, trashed: %b, received: %s }", id, seen, starred, trashed, received.toString());
    }

}