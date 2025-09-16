package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "AnswerOptions")
public class AnswerOptionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerOptionId;

    private String answerText;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private QuestionModel question;

}
