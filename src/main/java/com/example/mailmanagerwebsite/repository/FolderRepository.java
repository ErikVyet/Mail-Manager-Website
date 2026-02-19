package com.example.mailmanagerwebsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    public List<Folder> findByUserId(int userId);

}