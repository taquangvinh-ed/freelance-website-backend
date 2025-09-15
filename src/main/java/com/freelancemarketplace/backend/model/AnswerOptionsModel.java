package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnswerOptionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answer_id;

    private String answer_text;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionsModel question;

}
