package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "QA")
public class QandAModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qandaId;

    private String question;
    private String answer;
    private String tag;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private AdminModel admin;

}
