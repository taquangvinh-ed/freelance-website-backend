package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.model.Bio;
import com.freelancemarketplace.backend.model.SkillModel;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FreelancerDTO {

    private Long freelancerId;

    private String firstName;
    private String lastName;
    private String title;


    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private Boolean isVerified;
    private Double hourlyRate;

    private Bio bio;

    private Double wallet;
    private Integer connections;
    private Integer hoursPerWeek;
    private Boolean isBlocked;

    private Set<SkillModel> skills;
}
