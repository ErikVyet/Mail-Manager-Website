package com.example.mailmanagerwebsite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mailmanagerwebsite.dto.DetailDTO;
import com.example.mailmanagerwebsite.model.Detail;
import com.example.mailmanagerwebsite.repository.DetailRepository;

@Service
public class DetailService {

    protected final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
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

    public List<DetailDTO> getAllMails(int userId) {
        List<Detail> details = detailRepository.findByUserId(userId);
        List<DetailDTO> detailDTOs = new ArrayList<>();
        for (Detail detail : details) {
            detailDTOs.add(DetailDTO.convert(detail));
        }
        return detailDTOs;
    }

}