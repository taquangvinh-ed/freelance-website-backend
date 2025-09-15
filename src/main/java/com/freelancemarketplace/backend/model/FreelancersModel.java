package com.freelancemarketplace.backend.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FreelancersModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freelancer_id;

    private String first_name;
    private String last_name;
    private String title;


    private String profile_picture;
    private String email;
    private String phone_number;
    private String password_hash;
    private Boolean isVerified;
    private Double hourly_rate;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Bio bio;

    private Double wallet;
    private Integer connections;
    private Integer hours_per_week;
    private Boolean isBlocked;



    @OneToMany(mappedBy = "freelancer")
    private Set<VideosModel> videoList;

    @OneToOne(mappedBy = "freelancer")
    private PortfoliosModel portfolio;

    @ManyToMany
    @JoinTable(
            name = "freelancer_languages",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set <LanguagesModel> freelancer_languages;

    @OneToOne
    @JoinColumn(name = "analytics_id")
    private AnalyticsModel freelancer_analytics;

    @ManyToOne
    @JoinColumn(name="location_id")
    private LocationsModel freelancer_location;

    @OneToMany(mappedBy = "freelancerMessages")
    private  Set<MessagesModel> messagesList;

    @OneToMany(mappedBy = "contractFreelancer")
    private Set<ContractsModel> contractsList;

    @OneToMany(mappedBy = "projectFreelancer")
    private Set<ProjectsModel> projectsList;

    @ManyToMany
    @JoinTable(
            name = "freelancer_skills",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

    @OneToMany(mappedBy = "freelancerCertificates" )
    private Set<CertificatesModel> certificatesList;

    @OneToMany(mappedBy = "freelancerEducations" )
    private Set<EducationsModel> educationList;

    @OneToMany(mappedBy = "freelancerProposals" )
    private Set<ProposalsModal> proposalsList;

    @ManyToMany(mappedBy="freelancerTests" )
    private Set<TestsModel> testsList;

    @OneToMany(mappedBy = "freelancerTestimonials" )
    private Set<TestimonialsModel> testimonialsList;

    //Team that the freelancer is part of
    @ManyToMany(mappedBy = "teamFreelancers" )
    private Set<TeamsModel> teams;

    @OneToMany(mappedBy = "freelancerExperiences" )
    private Set<ExperiencesModel> experiencesList;

    @OneToMany(mappedBy = "freelancerPayments" )
    private Set<PaymentsModel> paymentsList;

    @OneToMany(mappedBy = "freelancerReports" )
    private Set<ReportsModel> reportsList;

    @OneToMany(mappedBy = "freelancerProduct")
    private Set<ProductsModel> productsList;

    @OneToMany(mappedBy = "freelancer")
    private Set<FreelancerTestResults> testResults;

}
