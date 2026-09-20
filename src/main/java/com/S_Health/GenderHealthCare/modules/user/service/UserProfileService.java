package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.modules.user.domain.User;


import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.request.UserProfileUpdateRequest;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.UserRepository;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.integrations.storage.ImageStorage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ModelMapper modelMapper;
    private final ImageStorage imageStorage;

    public UserProfileService(
            UserRepository userRepository,
            CurrentUserProvider currentUserProvider,
            ModelMapper modelMapper,
            ImageStorage imageStorage) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.modelMapper = modelMapper;
        this.imageStorage = imageStorage;
    }

    @Transactional
    public UserDetailResponse updateUserProfile(UserProfileUpdateRequest request) {
        Long userId = currentUserProvider.requireUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));

        if (request.getFullname() != null) user.setFullname(request.getFullname());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getImageUrl() != null) user.setImageUrl(request.getImageUrl());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());

        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserDetailResponse.class);
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserProfile() {
        Long userId = currentUserProvider.requireUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));
        return modelMapper.map(user, UserDetailResponse.class);
    }

    @Transactional
    public UserDetailResponse updateAvatar(MultipartFile file) {
        Long userId = currentUserProvider.requireUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.PROFILE_NOT_FOUND));

        try {
            String imageUrl = imageStorage.uploadImage(file);
            user.setImageUrl(imageUrl);
            User updated = userRepository.save(user);
            return modelMapper.map(updated, UserDetailResponse.class);
        } catch (IOException e) {
            throw new DomainException(ErrorCode.INTERNAL_ERROR, CommonMessages.IMAGE_UPLOAD_FAILED, e);
        }
    }
}
