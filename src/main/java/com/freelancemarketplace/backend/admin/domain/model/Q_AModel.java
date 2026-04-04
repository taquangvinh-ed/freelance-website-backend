package com.freelancemarketplace.backend.admin.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "QA")
public class Q_AModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qaId;

    private String question;

    private String answer;

    private String tag;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private AdminModel admin;

}
