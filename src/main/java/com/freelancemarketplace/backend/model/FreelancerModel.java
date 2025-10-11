package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Freelancers")
public class FreelancerModel extends BaseEntity {
    @Id
    private Long freelancerId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private UserModel user;

    private String firstName;
    private String lastName;
    private String title;
    private String profilePicture;
    private String phoneNumber;
    private Double hourlyRate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    private Double wallet;
    private Integer connections;
    private Integer hoursPerWeek;


    @OneToMany(mappedBy = "freelancer")
    private Set<VideoModel> videos;
    @OneToOne(mappedBy = "freelancer")
    private PortfolioModel portfolio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "freelancer_languages",
            joinColumns = @JoinColumn(name = "freelancerId"),
            inverseJoinColumns = @JoinColumn(name = "languageId")
    )
    private Set <LanguageModel> languages = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analyticId")
    private AnalyticModel analytic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="locationId")
    private LocationModel location;

    @OneToMany(mappedBy = "freelancer")
    private Set<ContractModel> contracts;

    @OneToMany(mappedBy = "winningFreelancer")
    private Set<ProjectModel> projects;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "freelancer_skills",
            joinColumns = @JoinColumn(name = "freelancerId"),
            inverseJoinColumns = @JoinColumn(name = "skillId")
    )
    private Set<SkillModel> skills = new HashSet<>();

    @OneToMany(mappedBy = "freelancer")
    private Set<CertificateModel> certificates;

    @OneToMany(mappedBy = "freelancer")
    private Set<EducationModel> educations;

    @OneToMany(mappedBy = "freelancer")
    private Set<ProposalModel> proposals;

    @OneToMany(mappedBy = "freelancer")
    private Set<TestimonialModel> testimonials;

    //Team that the freelancer is part of
    @ManyToMany(mappedBy = "members")
    private Set<TeamModel> teams;

    @OneToMany(mappedBy = "freelancer")
    private Set<ExperienceModel> experiences;

    @OneToMany(mappedBy = "freelancer")
    private Set<PaymentModel> payments;

    @OneToMany(mappedBy = "freelancer")
    private Set<ReportModel> reports;

    @OneToMany(mappedBy = "freelancer")
    private Set<ProductModel> products;

    @OneToMany(mappedBy = "freelancer")
    private Set<FreelancerTestResults> testResults;

    @Column(name = "profile_embedding", columnDefinition = "bytea")
    private byte[] profileEmbedding;

    @Column(name = "skill_vector", columnDefinition = "bytea")
    private byte[] skillVector;

    private String stripeCustomerId;


}
