package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.QuestionTags;
import com.freelancemarketplace.backend.enums.QuestionTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Questions")
public class QuestionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    private QuestionTags questionTag;

    @Enumerated(EnumType.STRING)
    private QuestionTypes questionType; // e.g., "text", "multiple-choice"

    private String question_text;

    @OneToMany(mappedBy = "question")
    private Set<AnswerOptionModel> options;

    @ManyToOne
    @JoinColumn(name = "testId")
    private TestModel test;


}
