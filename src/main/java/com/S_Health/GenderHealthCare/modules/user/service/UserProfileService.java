package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.modules.user.domain.User;


import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDTO;
import com.S_Health.GenderHealthCare.modules.user.dto.request.UserProfileUpdateRequest;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.repository.UserRepository;
import com.S_Health.GenderHealthCare.integrations.storage.ImageStorage;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final ImageStorage imageStorage;

    public UserProfileService(
            UserRepository userRepository,
            AuthUtil authUtil,
            ModelMapper modelMapper,
            ImageStorage imageStorage) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
        this.imageStorage = imageStorage;
    }

    @Transactional
    public UserDTO updateUserProfile(UserProfileUpdateRequest request) {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));

        if (request.getFullname() != null) user.setFullname(request.getFullname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getImageUrl() != null) user.setImageUrl(request.getImageUrl());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());

        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserProfile() {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));
        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    public UserDTO updateAvatar(MultipartFile file) {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));

        try {
            String imageUrl = imageStorage.uploadImage(file);
            user.setImageUrl(imageUrl);
            User updated = userRepository.save(user);
            return modelMapper.map(updated, UserDTO.class);
        } catch (IOException e) {
            throw new DomainException(ErrorCode.INTERNAL_ERROR, CommonMessages.IMAGE_UPLOAD_FAILED, e);
        }
    }
}
