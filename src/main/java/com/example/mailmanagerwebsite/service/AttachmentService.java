package com.example.mailmanagerwebsite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.AttachmentDTO;
import com.example.mailmanagerwebsite.model.Attachment;
import com.example.mailmanagerwebsite.repository.AttachmentRepository;
import com.example.mailmanagerwebsite.repository.EmailRepository;

@Service
public class AttachmentService {

    protected final AttachmentRepository attachmentRepository;
    protected final EmailRepository emailRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, EmailRepository emailRepository) {
        this.attachmentRepository = attachmentRepository;
        this.emailRepository = emailRepository;
    }

    @Transactional(readOnly = true)
    public List<AttachmentDTO> getAttachments(int emailId) {
        List<Attachment> attachments = this.attachmentRepository.findByEmailId(emailId);
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        for (Attachment attachment : attachments) {
            attachmentDTOs.add(AttachmentDTO.convert(attachment));
        }
        return attachmentDTOs;
    }

    public Optional<AttachmentDTO> createAttachment(AttachmentDTO attachmentDTO) {
        try {
            Attachment attachment = this.attachmentRepository.save(new Attachment(
                attachmentDTO.getName(),
                attachmentDTO.getData(),
                attachmentDTO.getSize(),
                attachmentDTO.getMime(),
                attachmentDTO.getUploaded()
            ));
            attachment.setEmail(this.emailRepository.findById(attachmentDTO.getEmail_id()).get());
            return Optional.of(AttachmentDTO.convert(attachment));
        }
        catch(Exception exception) {
            return Optional.empty();
        }
    }
    
    public boolean createAttachments(List<AttachmentDTO> attachmentDTOs) {
        List<Attachment> attachments = new ArrayList<>();
        try {
            for (AttachmentDTO attachmentDTO : attachmentDTOs) {
                Attachment attachment = AttachmentDTO.revert(attachmentDTO);
                attachment.setEmail(this.emailRepository.findById(attachmentDTO.getEmail_id()).get());
                attachments.add(attachment);
            }
            attachments = this.attachmentRepository.saveAll(attachments);
        }
        catch(Exception exception) {
            return false;
        }
        return true;
    }

    public boolean deleteAttachment(int attachmentId) {
        try {
            this.attachmentRepository.deleteById(attachmentId);
            return true;
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

}