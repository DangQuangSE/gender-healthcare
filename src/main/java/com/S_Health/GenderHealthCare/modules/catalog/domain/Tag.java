package com.S_Health.GenderHealthCare.modules.catalog.domain;

import com.S_Health.GenderHealthCare.modules.content.domain.Blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    String description;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    List<Blog> blogs;
    @Builder.Default
    Boolean isActive = true;
}
