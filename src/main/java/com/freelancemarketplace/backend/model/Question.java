package com.freelancemarketplace.backend.model;

import java.io.Serializable;

public class Question implements Serializable {
    private Long question_id;
    private String question_text;
    private String question_type; // e.g., "text", "multiple-choice"
    private String[] options;
}
