package com.example.mailmanagerwebsite.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "email")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "integer")
    private int id;

    @Column(name = "subject", nullable = false, columnDefinition = "varchar(255)")
    private String subject;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @Column(name = "sent", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime sent;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @OneToMany(mappedBy = "email")
    private List<Recipient> recipients;

    @OneToMany(mappedBy = "email")
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "email")
    private List<Detail> details;

    public Email() {
        this.subject = null;
        this.body = null;
        this.sent = null;
    }

    public Email(String subject, String body, LocalDateTime sent) {
        this.subject = subject;
        this.body = body;
        this.sent = sent;
    }

    public Email(int id, String subject, String body, LocalDateTime sent) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, subject: %s, body: %s, sent: %s }", id, subject, body, sent.toString());
    }

}