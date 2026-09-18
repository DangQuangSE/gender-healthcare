package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.TagRequest;
import com.S_Health.GenderHealthCare.modules.catalog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.catalog.controller.TagController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyTagController {
    private final TagService tagService;

    public LegacyTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_TAG)
    public ResponseEntity<TagDetailResponse> createTag(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_TAGS)
    public ResponseEntity<List<TagDetailResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_TAG)
    public ResponseEntity<TagDetailResponse> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_TAG)
    public ResponseEntity<TagDetailResponse> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_TAG)
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
