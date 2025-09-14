package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;

@Entity
public class QandAModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qanda_id;

    private String question;
    private String answer;
    private String tag;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminsModel admin;

    public QandAModel() {
    }

    public Long getQanda_id() {
        return qanda_id;
    }

    public void setQanda_id(Long qanda_id) {
        this.qanda_id = qanda_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public AdminsModel getAdmin() {
        return admin;
    }

    public void setAdmin(AdminsModel admin) {
        this.admin = admin;
    }
}
