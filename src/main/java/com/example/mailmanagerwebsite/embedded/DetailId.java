package com.example.mailmanagerwebsite.embedded;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DetailId {

    @Column(name = "email_id", nullable = false, columnDefinition = "integer")
    private int email_id;

    @Column(name = "folder_id", nullable = false, columnDefinition = "integer")
    private int folder_id;

    public DetailId() { }

    public DetailId(int email_id, int folder_id) {
        this.email_id = email_id;
        this.folder_id = folder_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email_id, folder_id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DetailId other = (DetailId) obj;
        if (email_id != other.email_id)
            return false;
        if (folder_id != other.folder_id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "{ email_id: " + email_id + ", folder_id: " + folder_id + " }";
    }

}