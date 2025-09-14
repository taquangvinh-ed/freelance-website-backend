package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class TeamsModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private String team_name;
    private String team_description;
    private String team_leader;
    private Integer hourly_rate;
    private String hour_per_week;
    private String team_avatar;
    private String team_banner;
    private Integer connections;
    private double wallet;
    private Boolean is_verified;

    @OneToMany(mappedBy = "team")
    private List<VideosModel> videos;

    @ManyToMany
    @JoinTable(
        name = "team_freelancers",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private List<FreelancersModel> teamFreelancers;


    @OneToMany(mappedBy = "teamTestimonials")
    private Set<TestimonialsModel> testimonials;

    @OneToMany(mappedBy = "teamProposals")
    private Set<ProposalsModal> proposalsList;

    @OneToMany(mappedBy = "teamPayments")
    private Set<PaymentsModel> paymentsTeam;

    @OneToMany(mappedBy = "contractTeam")
    private Set<ContractsModel> contractsTeam;

    @OneToMany(mappedBy = "teamMessages")
    private Set<MessagesModel> messages;

    @ManyToMany
    @JoinTable(
        name = "team_skills",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

    public TeamsModel() {

    }

    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_description() {
        return team_description;
    }

    public void setTeam_description(String team_description) {
        this.team_description = team_description;
    }

    public String getTeam_leader() {
        return team_leader;
    }

    public void setTeam_leader(String team_leader) {
        this.team_leader = team_leader;
    }

    public Integer getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(Integer hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public String getHour_per_week() {
        return hour_per_week;
    }

    public void setHour_per_week(String hour_per_week) {
        this.hour_per_week = hour_per_week;
    }

    public String getTeam_avatar() {
        return team_avatar;
    }

    public void setTeam_avatar(String team_avatar) {
        this.team_avatar = team_avatar;
    }

    public String getTeam_banner() {
        return team_banner;
    }

    public void setTeam_banner(String team_banner) {
        this.team_banner = team_banner;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }

    public List<VideosModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosModel> videos) {
        this.videos = videos;
    }

    public List<FreelancersModel> getTeamFreelancers() {
        return teamFreelancers;
    }

    public void setTeamFreelancers(List<FreelancersModel> teamFreelancers) {
        this.teamFreelancers = teamFreelancers;
    }

    public Set<TestimonialsModel> getTestimonials() {
        return testimonials;
    }

    public void setTestimonials(Set<TestimonialsModel> testimonials) {
        this.testimonials = testimonials;
    }

    public Set<ProposalsModal> getProposalsList() {
        return proposalsList;
    }

    public void setProposalsList(Set<ProposalsModal> proposalsList) {
        this.proposalsList = proposalsList;
    }

    public Set<PaymentsModel> getPaymentsTeam() {
        return paymentsTeam;
    }

    public void setPaymentsTeam(Set<PaymentsModel> paymentsTeam) {
        this.paymentsTeam = paymentsTeam;
    }

    public Set<ContractsModel> getContractsTeam() {
        return contractsTeam;
    }

    public void setContractsTeam(Set<ContractsModel> contractsTeam) {
        this.contractsTeam = contractsTeam;
    }

    public Set<MessagesModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessagesModel> messages) {
        this.messages = messages;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }
}
