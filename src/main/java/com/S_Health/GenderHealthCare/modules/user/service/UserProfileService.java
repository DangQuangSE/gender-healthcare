package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.modules.user.domain.User;


import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.repository.UserRepository;
import com.S_Health.GenderHealthCare.integrations.storage.CloudinaryService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public UserProfileService(
            UserRepository userRepository,
            AuthUtil authUtil,
            ModelMapper modelMapper,
            CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    public UserDTO updateUserProfile(UserDTO request) {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("Người dùng không tồn tại"));

        if (request.getImg() != null) {
            try {
                String imageUrl = cloudinaryService.uploadImage(request.getImg());
                request.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new AppException("Không thể tải lên hình ảnh: " + e.getMessage());
            }
        }

        if (request.getFullname() != null) user.setFullname(request.getFullname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getImageUrl() != null) user.setImageUrl(request.getImageUrl());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());

        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserDTO.class);
    }

    public UserDTO getUserProfile() {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("Người dùng không tồn tại"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO updateAvatar(MultipartFile file) {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("Người dùng không tồn tại"));

        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            user.setImageUrl(imageUrl);
            User updated = userRepository.save(user);
            return modelMapper.map(updated, UserDTO.class);
        } catch (IOException e) {
            throw new AppException("Không thể tải lên hình ảnh: " + e.getMessage());
        }
    }
}
