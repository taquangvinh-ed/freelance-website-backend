package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

}
