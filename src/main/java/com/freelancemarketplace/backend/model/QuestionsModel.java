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
@Table(name = "questions")
public class QuestionsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long question_id;

    @Enumerated(EnumType.STRING)
    private QuestionTags question_tag;

    @Enumerated(EnumType.STRING)
    private QuestionTypes question_type; // e.g., "text", "multiple-choice"

    private String question_text;

    @OneToMany(mappedBy = "question")
    private Set<AnswerOptionsModel> options;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private TestsModel test;


}
