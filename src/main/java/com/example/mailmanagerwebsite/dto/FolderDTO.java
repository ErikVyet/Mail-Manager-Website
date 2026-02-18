package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.model.Folder;

public class FolderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private boolean system;
    private LocalDateTime created;

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

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public static FolderDTO convert(Folder folder) {
        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setId(folder.getId());
        folderDTO.setName(folder.getName());
        folderDTO.setSystem(folder.isSystem());
        folderDTO.setCreated(folder.getCreated());
        return folderDTO;
    }

    public static Folder revert(FolderDTO folderDTO) {
        Folder folder = new Folder();
        folder.setId(folderDTO.getId());
        folder.setName(folderDTO.getName());
        folder.setSystem(folderDTO.isSystem());
        folder.setCreated(folderDTO.getCreated());
        return folder;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, name: %s, system: %b, created: %s }", id, name, system, created.toString());
    }

}