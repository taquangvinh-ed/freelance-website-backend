package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class ProductsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;
    private String description;
    private Double price;

    @Enumerated(EnumType.STRING)
    private TailentTypes owner_type; // freelancer or team

    @Enumerated(EnumType.STRING)
    private RecuiterTypes buyer_type; // individual or company

    private String image;

    // Link to the product (e.g., URL to download or view the product)
    private String product_link;

    private Long views;
    private Long time_of_download;

    @OneToMany(mappedBy = "product")
    private Set<VideosModel> videos;

    //The freelancer who creates the product
    @ManyToOne
    @JoinColumn(name="freelancer_id")
    private FreelancersModel freelancerProduct;

    @OneToMany(mappedBy = "productMessages")
    private Set<MessagesModel> messages;

    @ManyToMany(mappedBy = "productsList" )
    private Set<CompaniesModel> companyList;

    @ManyToMany
    @JoinTable(
            name = "product_skills",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillsModel> skills;

    //The client who buys the product
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel clientProduct;


    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TailentTypes getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(TailentTypes owner_type) {
        this.owner_type = owner_type;
    }

    public RecuiterTypes getBuyer_type() {
        return buyer_type;
    }

    public void setBuyer_type(RecuiterTypes buyer_type) {
        this.buyer_type = buyer_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_link() {
        return product_link;
    }

    public void setProduct_link(String product_link) {
        this.product_link = product_link;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getTime_of_download() {
        return time_of_download;
    }

    public void setTime_of_download(Long time_of_download) {
        this.time_of_download = time_of_download;
    }

    public Set<VideosModel> getVideos() {
        return videos;
    }

    public void setVideos(Set<VideosModel> videos) {
        this.videos = videos;
    }

    public FreelancersModel getFreelancerProduct() {
        return freelancerProduct;
    }

    public void setFreelancerProduct(FreelancersModel freelancerProduct) {
        this.freelancerProduct = freelancerProduct;
    }

    public Set<MessagesModel> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessagesModel> messages) {
        this.messages = messages;
    }

    public Set<CompaniesModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(Set<CompaniesModel> companyList) {
        this.companyList = companyList;
    }

    public Set<SkillsModel> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillsModel> skills) {
        this.skills = skills;
    }

    public ClientsModel getClientProduct() {
        return clientProduct;
    }

    public void setClientProduct(ClientsModel clientProduct) {
        this.clientProduct = clientProduct;
    }
}
