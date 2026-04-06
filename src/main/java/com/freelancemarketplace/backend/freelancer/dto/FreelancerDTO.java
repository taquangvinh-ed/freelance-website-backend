package com.freelancemarketplace.backend.freelancer.dto;

import com.freelancemarketplace.backend.freelancer.domain.model.Bio;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import com.freelancemarketplace.backend.certification.dto.CertificateDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;
import com.freelancemarketplace.backend.user.dto.UserDTO;

@Getter
@Setter
@NoArgsConstructor
public class FreelancerDTO {

    private Long freelancerId;

    private String firstName;
    private String lastName;
    private String title;
    private String profilePicture;
    private String avatar;
    private String phoneNumber;
    private String passwordHash;
    private Double hourlyRate;

    private Bio bio;

    private Double wallet;
    private Integer connections;
    private Integer hoursPerWeek;

    private UserDTO user;

    private Set<SkillDTO> skills = new HashSet<>();
    private Set<CertificateDTO>certificates= new HashSet<>();
    private Set<ExperienceDTO>experiences = new HashSet<>();

    private String stripeCustomerId;

    private Timestamp createdAt;

    private double averageScore;

    private int reviews;
}

