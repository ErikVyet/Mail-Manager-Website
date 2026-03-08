package com.example.mailmanagerwebsite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.example.mailmanagerwebsite.repository.EmailRepository;
import com.example.mailmanagerwebsite.repository.FolderRepository;
import com.example.mailmanagerwebsite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.DetailDTO;
import com.example.mailmanagerwebsite.embedded.DetailId;
import com.example.mailmanagerwebsite.model.Detail;
import com.example.mailmanagerwebsite.repository.DetailRepository;

@Service
public class DetailService {

    protected final EmailRepository emailRepository;

    protected final FolderRepository folderRepository;

    protected final UserRepository userRepository;

    protected final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository, UserRepository userRepository, FolderRepository folderRepository, EmailRepository emailRepository) {
        this.detailRepository = detailRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.emailRepository = emailRepository;
    }

    @Transactional(readOnly = true)
    public List<DetailDTO> getDetailsByFolderId(int userId, int folderId) {
        List<DetailDTO> detailDTOs = new ArrayList<>();
        switch (folderId) {
            case -1: {
                return getStarredMails(userId);
            }
            case -2: {
                return getAllMails(userId);
            }
            default: {
                List<Detail> details = detailRepository.findByFolderId(folderId);
                for (Detail detail : details) {
                    detailDTOs.add(DetailDTO.convert(detail));
                }
                break;
            }
        }
        return detailDTOs;
    }

    @Transactional(readOnly = true)
    public List<DetailDTO> getStarredMails(int userId) {
        List<Detail> details = detailRepository.findByUserIdAndStarredTrue(userId);
        List<DetailDTO> detailDTOs = new ArrayList<>();
        for (Detail detail : details) {
            detailDTOs.add(DetailDTO.convert(detail));
        }
        return detailDTOs;
    }

    @Transactional(readOnly = true)
    public List<DetailDTO> getAllMails(int userId) {
        List<Detail> details = detailRepository.findByUserId(userId);
        List<DetailDTO> detailDTOs = new ArrayList<>();
        for (Detail detail : details) {
            detailDTOs.add(DetailDTO.convert(detail));
        }
        return detailDTOs;
    }

    public Optional<DetailDTO> createDraftDetail(int userId, int mailId) {
        try {
            Detail detail = new Detail();
            detail.setId(new DetailId());
            detail.setReceived(null);
            detail.setSeen(true);
            detail.setStarred(false);
            detail.setEmail(this.emailRepository.findById(mailId).get());
            detail.setFolder(this.folderRepository.findByUserIdAndName(userId, "Drafts").get());
            return Optional.of(DetailDTO.convert(this.detailRepository.save(detail)));
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            return Optional.empty();
        }
    }

}