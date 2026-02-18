package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;

import com.example.mailmanagerwebsite.embedded.RecipientId;
import com.example.mailmanagerwebsite.enums.Type;
import com.example.mailmanagerwebsite.model.Recipient;

public class RecipientDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private RecipientId id;
    private Type type;

    public RecipientId getId() {
        return id;
    }

    public void setId(RecipientId id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static RecipientDTO convert(Recipient recipient) {
        RecipientDTO recipientDTO = new RecipientDTO();
        recipientDTO.setId(recipient.getId());
        recipientDTO.setType(recipient.getType());
        return recipientDTO;
    }

    public static Recipient revert(RecipientDTO recipientDTO) {
        Recipient recipient = new Recipient();
        recipient.setId(recipientDTO.getId());
        recipient.setType(recipientDTO.getType());
        return recipient;
    }

    @Override
    public String toString() {
        return String.format("{ email_id: %d, user_id: %d, type: %s }", id.getEmail_id(), id.getUser_id(), type.toString());
    }

}