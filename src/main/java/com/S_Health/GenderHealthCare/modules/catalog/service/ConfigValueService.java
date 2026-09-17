package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.entity.ConfigValue;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.repository.ConfigValueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigValueService {

    private final ConfigValueRepository configValueRepository;

    public ConfigValueService(ConfigValueRepository configValueRepository) {
        this.configValueRepository = configValueRepository;
    }

    /**
     * Lấy tất cả cấu hình
     */
    public List<ConfigValue> getAllConfigs() {
        return configValueRepository.findAll();
    }

    /**
     * Tạo cấu hình mới
     */
    public ConfigValue createConfig(String name, Integer value) {
        if (configValueRepository.existsByName(name)) {
            throw new AppException(CatalogConstants.CONFIG_EXISTS);
        }
        ConfigValue config = ConfigValue.builder()
                .name(name)
                .value(value)
                .build();
        return configValueRepository.save(config);
    }

    /**
     * Cập nhật cấu hình
     */
    public ConfigValue updateConfig(Long id, Integer value) {
        ConfigValue config = configValueRepository.findById(id)
                .orElseThrow(() -> new AppException(CatalogConstants.CONFIG_NOT_FOUND));

        config.setValue(value);
        return configValueRepository.save(config);
    }

    /**
     * Xóa cấu hình
     */
    public void deleteConfig(Long id) {
        if (!configValueRepository.existsById(id)) {
            throw new AppException(CatalogConstants.CONFIG_NOT_FOUND);
        }

        configValueRepository.deleteById(id);
    }

    /**
     * Lấy giá trị cấu hình theo tên
     */
    public Integer getConfigValue(String name, Integer defaultValue) {
        return configValueRepository.findByName(name)
                .map(ConfigValue::getValue)
                .orElse(defaultValue);
    }
}
