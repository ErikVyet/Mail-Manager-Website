package com.example.mailmanagerwebsite.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.mailmanagerwebsite.model.Folder;
import com.example.mailmanagerwebsite.validation.NotBlankValidation;
import com.example.mailmanagerwebsite.validation.NotNullValidation;
import com.example.mailmanagerwebsite.validation.SizeValidation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FolderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    @NotNull(message = "Folder name need to be specified", groups = NotNullValidation.class)
    @NotBlank(message = "Folder name is required", groups = NotBlankValidation.class)
    @Size(min = 1, max = 20, message = "Folder name must be between 1 and 20 characters", groups = SizeValidation.class)
    private String name;

    @NotNull(message = "Type of folder need to be specified", groups = NotNullValidation.class)
    private boolean system;

    @NotNull(message = "Created date need to be specified", groups = NotNullValidation.class)
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