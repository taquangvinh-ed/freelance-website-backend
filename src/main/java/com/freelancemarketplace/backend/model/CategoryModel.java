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
@Table(name = "Categories")
public class CategoryModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    private String image;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "category_skill",
            joinColumns = @JoinColumn(name="categoryId"),
            inverseJoinColumns = @JoinColumn(name="skillId")
    )
    private Set<SkillModel> skills;

    @OneToMany(mappedBy = "category")
    private Set<ProjectModel> projects;

}
