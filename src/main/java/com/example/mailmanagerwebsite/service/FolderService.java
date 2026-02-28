package com.example.mailmanagerwebsite.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.FolderDTO;
import com.example.mailmanagerwebsite.model.Folder;
import com.example.mailmanagerwebsite.model.User;
import com.example.mailmanagerwebsite.repository.DetailRepository;
import com.example.mailmanagerwebsite.repository.FolderRepository;
import com.example.mailmanagerwebsite.repository.UserRepository;

@Service
public class FolderService {

    protected final FolderRepository folderRepository;
    protected final DetailRepository detailRepository;
    protected final UserRepository userRepository;

    public FolderService(FolderRepository folderRepository, DetailRepository detailRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.detailRepository = detailRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<FolderDTO> getFolder(int folderId, int userId) {
        Optional<Folder> optFolder = this.folderRepository.findByUserIdAndId(userId, folderId);
        Optional<FolderDTO> opt = Optional.empty();
        if (optFolder.isPresent()) {
            opt = Optional.of(FolderDTO.convert(optFolder.get()));
        }
        return opt;
    }

    @Transactional(readOnly = true)
    public Optional<FolderDTO> getFolder(String name, int userId) {
        Optional<Folder> optFolder = this.folderRepository.findByUserIdAndName(userId, name);
        Optional<FolderDTO> opt = Optional.empty();
        if (optFolder.isPresent()) {
            opt = Optional.of(FolderDTO.convert(optFolder.get()));
        }
        return opt;
    }

    @Transactional(readOnly = true)
    public List<FolderDTO> getFolders(int userId) {
        List<Folder> folders = this.folderRepository.findByUserId(userId);
        List<FolderDTO> folderDTOs = new ArrayList<>();
        for (Folder folder : folders) {
            folderDTOs.add(FolderDTO.convert(folder));
        }
        return folderDTOs;
    }

    @Transactional(readOnly = true)
    public List<FolderDTO> getSystemFolders(int userId) {
        List<Folder> folders = this.folderRepository.findBySystemAndUserId(true, userId);
        List<FolderDTO> folderDTOs = new ArrayList<>();
        for (Folder folder : folders) {
            folderDTOs.add(FolderDTO.convert(folder));
        }
        return folderDTOs;
    }

    @Transactional(readOnly = true)
    public List<FolderDTO> getCustomFolders(int userId) {
        List<Folder> folders = this.folderRepository.findBySystemAndUserId(false, userId);
        List<FolderDTO> folderDTOs = new ArrayList<>();
        for (Folder folder : folders) {
            folderDTOs.add(FolderDTO.convert(folder));
        }
        return folderDTOs;
    }

    @Transactional
    public boolean createFolder(FolderDTO folderDTO, int userId) {
        Optional<User> optUser = this.userRepository.findById(userId);
        if (!optUser.isPresent()) {
            return false;
        }
        if (this.folderRepository.findByUserIdAndName(userId, folderDTO.getName()).isPresent()) {
            return false;
        }
        if (this.folderRepository.findBySystemAndUserId(false, userId).size() >= 7) {
            return false;
        }

        Folder folder = new Folder(
            folderDTO.getName(),
            folderDTO.isSystem(),
            LocalDateTime.now()
        );
        folder.setUser(optUser.get());
        try {
            folderDTO.setId(this.folderRepository.save(folder).getId());;
        } 
        catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

    @Transactional
    public boolean deleteFolder(int folderId, int userId) {
        Optional<Folder> optFolder = this.folderRepository.findByUserIdAndId(userId, folderId);
        if (!optFolder.isPresent() || optFolder.get().isSystem()) {
            return false;
        }
        try {
            this.detailRepository.deleteByFolderId(folderId);
            this.folderRepository.delete(optFolder.get());
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

}