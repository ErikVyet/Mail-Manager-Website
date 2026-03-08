package com.example.mailmanagerwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

    public List<Attachment> findByEmailId(int emailId);

}