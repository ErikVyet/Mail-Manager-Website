package com.example.mailmanagerwebsite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mailmanagerwebsite.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    public List<Folder> findByUserId(int userId);
    public List<Folder> findBySystemAndUserId(boolean system, int userId);
    public Optional<Folder> findByUserIdAndId(int userId, int id);
    public Optional<Folder> findByUserIdAndName(int userId, String name);

}