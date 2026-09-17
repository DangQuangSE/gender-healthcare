package com.S_Health.GenderHealthCare.modules.catalog.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.TagRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
    private final CatalogService catalogService;

    public TagController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = "Get all tags")
    public ApiResponse<List<TagResponse>> getTags() {
        return ApiResponse.success(catalogService.getTags(), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID")
    public ApiResponse<TagResponse> getTag(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getTag(id), null);
    }

    @PostMapping
    @Operation(summary = "Create tag")
    public ApiResponse<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        return ApiResponse.success(catalogService.createTag(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tag")
    public ApiResponse<TagResponse> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request) {
        return ApiResponse.success(catalogService.updateTag(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tag")
    public ApiResponse<String> deleteTag(@PathVariable Long id) {
        catalogService.deleteTag(id);
        return ApiResponse.success(CatalogConstants.TAG_DELETED, null);
    }
}
