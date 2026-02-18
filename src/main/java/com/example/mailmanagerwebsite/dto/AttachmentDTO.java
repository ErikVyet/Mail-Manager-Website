package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.model.Attachment;

public class AttachmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private byte[] data;
    private int size;
    private String mime;
    private LocalDateTime uploaded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public static AttachmentDTO convert(Attachment attachment) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setName(attachment.getName());
        attachmentDTO.setData(attachment.getData());
        attachmentDTO.setSize(attachment.getSize());
        attachmentDTO.setMime(attachment.getMime());
        attachmentDTO.setUploaded(attachment.getUploaded());
        return attachmentDTO;
    }

    public static Attachment revert(AttachmentDTO attachmentDTO) {
        Attachment attachment = new Attachment();
        attachment.setId(attachmentDTO.getId());
        attachment.setName(attachmentDTO.getName());
        attachment.setData(attachmentDTO.getData());
        attachment.setSize(attachmentDTO.getSize());
        attachment.setMime(attachmentDTO.getMime());
        attachment.setUploaded(attachmentDTO.getUploaded());
        return attachment;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, name: %s, mime: %s, size: %d, uploaded: %s, data: %s }", id, name, mime, size, uploaded.toString(), data.toString());
    }

}
