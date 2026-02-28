package com.example.mailmanagerwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.embedded.DetailId;
import com.example.mailmanagerwebsite.model.Detail;

public interface DetailRepository extends JpaRepository<Detail, DetailId> {

    public List<Detail> findByFolderId(int folderId);

    public List<Detail> findByUserIdAndStarredTrue(int userId);

    public List<Detail> findByUserId(int userId);

    public void deleteByFolderId(int folderId);

}