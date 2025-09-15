package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CategoriesModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    private String category_name;

    private String category_image;


    @ManyToMany
    @JoinTable(
            name = "category_skill",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="skill_id")
    )
    private Set<SkillsModel> skills;

    @OneToMany(mappedBy = "category")
    private Set<ProjectsModel> projects;

}
